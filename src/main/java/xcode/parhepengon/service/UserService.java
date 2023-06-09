package xcode.parhepengon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.parhepengon.domain.mapper.CommonMapper;
import xcode.parhepengon.domain.mapper.UserMapper;
import xcode.parhepengon.domain.model.*;
import xcode.parhepengon.domain.repository.OtpRepository;
import xcode.parhepengon.domain.repository.ResetRepository;
import xcode.parhepengon.domain.repository.TokenRepository;
import xcode.parhepengon.domain.repository.UserRepository;
import xcode.parhepengon.domain.request.auth.*;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.domain.response.auth.LoginResponse;
import xcode.parhepengon.domain.response.auth.RegisterResponse;
import xcode.parhepengon.exception.AppException;
import xcode.parhepengon.presenter.UserPresenter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static xcode.parhepengon.domain.enums.EventEnum.*;
import static xcode.parhepengon.shared.ResponseCode.*;
import static xcode.parhepengon.shared.Utils.*;

@Service
public class UserService implements UserPresenter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private ResetRepository resetRepository;

    private final UserMapper userMapper = new UserMapper();
    private final CommonMapper commonMapper = new CommonMapper();

    @Override
    public BaseResponse<LoginResponse> login(LoginRequest request) {
        BaseResponse<LoginResponse> response = new BaseResponse<>();

        Optional<UserModel> model = userRepository.findByUsernameOrEmailAndActiveIsTrueAndDeletedAtIsNull(request.getUsername(), request.getUsername());

        if (model.isEmpty() || !Objects.equals(encryptor(model.get().getPassword(), false), request.getPassword())) {
            throw new AppException(AUTH_ERROR_MESSAGE);
        }

        try {
            String token = jwtService.generateToken(model.get());
            tokenRepository.save(new TokenModel(
                    token,
                    model.get().getSecureId(),
                    false
            ));

            historyService.addHistory(LOGIN, model.get().getSecureId());

            response.setSuccess(userMapper.userModelToLoginResponse(model.get(), token));
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    @Override
    public BaseResponse<RegisterResponse> register(RegisterRequest request) {
        BaseResponse<RegisterResponse> response = new BaseResponse<>();

        if (userRepository.findByEmailAndActiveIsTrueAndDeletedAtIsNull(request.getEmail()).isPresent()) {
            throw new AppException(EMAIL_EXIST);
        }

        if (userRepository.findByUsernameAndActiveIsTrueAndDeletedAtIsNull(request.getUsername()).isPresent()) {
            throw new AppException(USERNAME_EXIST);
        }

        try {
            UserModel model = userMapper.registerRequestToUserModel(request);
            userRepository.save(model);

            String token = jwtService.generateToken(model);

            tokenRepository.save(new TokenModel(
                    token,
                    model.getSecureId(),
                    true
            ));

            OtpModel otpModel = userMapper.userModelToOtpModel(model);

            otpRepository.save(otpModel);
            emailService.sendOtpEmail(request.getEmail(), otpModel.getCode());

            response.setSuccess(userMapper.userModelToRegisterResponse(token));
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    @Override
    public BaseResponse<Boolean> verifyOtp(VerifyOtpRequest request) {
        BaseResponse<Boolean> response = new BaseResponse<>();

        Optional<UserModel> userModel = userRepository.findBySecureIdAndDeletedAtIsNull(CurrentUser.get().getUserSecureId());

        if (!otpRepository.existsByUserSecureIdAndVerifiedIsFalse(CurrentUser.get().getUserSecureId()) || userModel.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        try {
            Optional<OtpModel> otpModel = otpRepository.findByUserSecureIdAndVerifiedIsFalse(userModel.get().getSecureId());
            Optional<TokenModel> tokenModel = tokenRepository.findByTokenAndTemporaryIsTrue(CurrentUser.get().getToken());

            if (otpModel.isEmpty()) {
                throw new AppException(NOT_FOUND_MESSAGE);
            }

            if (!otpModel.get().getCode().equals(request.getOtp()) || !otpModel.get().isValid()) {
                throw new AppException(OTP_ERROR_MESSAGE);
            }

            otpModel.get().setVerified(true);
            userModel.get().setActive(true);

            userRepository.save(userModel.get());
            otpRepository.save(otpModel.get());
            tokenModel.ifPresent(tokenRepository::delete);

            historyService.addHistory(REGISTER, userModel.get().getSecureId());

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    @Override
    public BaseResponse<Boolean> resendOtp() {
        BaseResponse<Boolean> response = new BaseResponse<>();

        Optional<TokenModel> tokenModel = tokenRepository.findByTokenAndTemporaryIsTrue(CurrentUser.get().getToken());
        Optional<OtpModel> otpModel = otpRepository.findByUserSecureIdAndVerifiedIsFalse(CurrentUser.get().getUserSecureId());
        Optional<UserModel> userModel = userRepository.findBySecureIdAndDeletedAtIsNull(CurrentUser.get().getUserSecureId());

        if (!otpRepository.existsByUserSecureIdAndVerifiedIsFalse(CurrentUser.get().getUserSecureId()) || tokenModel.isEmpty() || otpModel.isEmpty() || userModel.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        try {
            String newOtp = generateOTP();
            otpModel.get().setCode(newOtp);
            tokenModel.get().setExpireAt(getTemporaryDate());

            otpRepository.save(otpModel.get());
            tokenRepository.save(tokenModel.get());
            emailService.sendOtpEmail(userModel.get().getEmail(), newOtp);

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    @Override
    public BaseResponse<Boolean> logout() {
        BaseResponse<Boolean> response = new BaseResponse<>();

        Optional<TokenModel> tokenModel = tokenRepository.findByToken(CurrentUser.get().getToken());

        if (tokenModel.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        try {
            tokenModel.ifPresent(tokenRepository::delete);
            historyService.addHistory(LOGOUT, null);

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    @Override
    public BaseResponse<Boolean> forgotPassword(ForgotPasswordRequest request) {
        BaseResponse<Boolean> response = new BaseResponse<>();

        Optional<UserModel> userModel = userRepository.findByEmailAndActiveIsTrueAndDeletedAtIsNull(request.getEmail());

        if (userModel.isEmpty()) {
            throw new AppException(EMAIL_NOT_FOUND);
        }

        try {
            String code = generateSecureCharId();

            resetRepository.save(commonMapper.generateResetModel(request.getEmail(), code));
            emailService.sendResetPasswordEmail(request.getEmail(), code);

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    @Override
    public BaseResponse<Boolean> resetPassword(ResetPasswordRequest request) {
        BaseResponse<Boolean> response = new BaseResponse<>();

        List<ResetModel> resetModelList = resetRepository.findByCodeAndVerifiedIsFalse(request.getCode());
        ResetModel resetModel = null;

        for (ResetModel model : resetModelList) {
            if (model.isValid()) {
                resetModel = model;
                break;
            }
        }

        if (resetModel == null) {
            throw new AppException(INVALID_CODE);
        }

        Optional<UserModel> userModel = userRepository.findByEmailAndActiveIsTrueAndDeletedAtIsNull(resetModel.getEmail());

        if (userModel.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        try {
            userRepository.save(userMapper.resetPasswordRequestToUserModel(userModel.get(), request.getNewPassword()));

            resetModel.setVerified(true);
            resetRepository.save(resetModel);

            historyService.addHistory(RESET_PASSWORD, userModel.get().getSecureId());

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    @Override
    public BaseResponse<LoginResponse> getProfile() {
        BaseResponse<LoginResponse> response = new BaseResponse<>();

        Optional<UserModel> model = userRepository.findBySecureIdAndDeletedAtIsNull(CurrentUser.get().getUserSecureId());

        if (model.isEmpty()) {
            throw new AppException(AUTH_ERROR_MESSAGE);
        }

        response.setSuccess(userMapper.userModelToLoginResponse(model.get(), null));

        return response;
    }

    @Override
    public BaseResponse<Boolean> editProfile(EditProfileRequest request) {
        BaseResponse<Boolean> response = new BaseResponse<>();

        Optional<UserModel> model = userRepository.findBySecureIdAndDeletedAtIsNull(CurrentUser.get().getUserSecureId());

        if (model.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        try {
            userRepository.save(userMapper.editProfileRequestToUserModel(request, model.get()));

            historyService.addHistory(EDIT_PROFILE, null);

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    @Override
    public BaseResponse<Boolean> changePassword(ChangePasswordRequest request) {
        BaseResponse<Boolean> response = new BaseResponse<>();

        Optional<UserModel> model = userRepository.findBySecureIdAndDeletedAtIsNull(CurrentUser.get().getUserSecureId());

        if (model.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        if (!Objects.equals(encryptor(model.get().getPassword(), false), request.getOldPassword())) {
            throw new AppException(AUTH_ERROR_MESSAGE);
        }

        try {
            userRepository.save(userMapper.changePasswordRequestToUserModel(request));

            historyService.addHistory(CHANGE_PASSWORD, null);

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    public String getCurrentUserPassword() {
        Optional<UserModel> userModel = userRepository.findBySecureIdAndDeletedAtIsNull(CurrentUser.get().getUserSecureId());

        return userModel.map(model -> encryptor(model.getPassword(), false)).orElse(null);
    }
}
