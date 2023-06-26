package xcode.parhepengon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.parhepengon.domain.mapper.CommonMapper;
import xcode.parhepengon.domain.mapper.ProfileMapper;
import xcode.parhepengon.domain.mapper.UserMapper;
import xcode.parhepengon.domain.model.*;
import xcode.parhepengon.domain.repository.*;
import xcode.parhepengon.domain.request.auth.*;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.domain.response.auth.LoginResponse;
import xcode.parhepengon.domain.response.auth.RegisterResponse;
import xcode.parhepengon.exception.AppException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static xcode.parhepengon.domain.enums.AccountHistoryEventEnum.*;
import static xcode.parhepengon.shared.ResponseCode.*;
import static xcode.parhepengon.shared.Utils.*;

@Service
public class UserService {
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

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private SettingRepository settingRepository;

    private final UserMapper userMapper = new UserMapper();
    private final CommonMapper commonMapper = new CommonMapper();
    private final ProfileMapper profileMapper = new ProfileMapper();

    public BaseResponse<LoginResponse> login(LoginRequest request) {
        BaseResponse<LoginResponse> response = new BaseResponse<>();

        Optional<UserModel> model = userRepository.getActiveUserByUsername(request.getUsername());

        if (model.isEmpty() || !Objects.equals(encryptor(model.get().getPassword(), false), request.getPassword())) {
            throw new AppException(AUTH_ERROR_MESSAGE);
        }

        Optional<ProfileModel> profileModel = profileRepository.getProfileBySecureId(model.get().getSecureId());

        if (profileModel.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        try {
            String token = jwtService.generateToken(model.get());
            tokenRepository.save(new TokenModel(
                    token,
                    model.get().getSecureId(),
                    false
            ));

            historyService.addAccountHistory(LOGIN, model.get().getSecureId());

            LoginResponse loginResponse = userMapper.userModelToLoginResponse(model.get(), token);
            loginResponse.setPhone(profileModel.get().getPhone());
            loginResponse.setFullName(profileModel.get().getFullName());
            loginResponse.setEmail(profileModel.get().getEmail());

            response.setSuccess(loginResponse);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    public BaseResponse<RegisterResponse> register(RegisterRequest request) {
        BaseResponse<RegisterResponse> response = new BaseResponse<>();

        Optional<ProfileModel> model = profileRepository.getProfileByEmail(request.getEmail());
        Optional<UserModel> userModel = userRepository.getActiveUserBySecureId(model.map(ProfileModel::getEmail).orElse(null));

        if (userModel.isPresent()) {
            throw new AppException(EMAIL_EXIST);
        }

        if (userRepository.getActiveUserByUsername(request.getUsername()).isPresent()) {
            throw new AppException(USERNAME_EXIST);
        }

        try {
            UserModel newModel = userMapper.registerRequestToUserModel(request);
            ProfileModel profileModel = profileMapper.registerRequestToProfileModel(request, newModel.getSecureId());
            userRepository.save(newModel);
            profileRepository.save(profileModel);

            String token = jwtService.generateToken(newModel);

            tokenRepository.save(new TokenModel(
                    token,
                    newModel.getSecureId(),
                    true
            ));

            OtpModel otpModel = userMapper.userModelToOtpModel(newModel);

            otpRepository.save(otpModel);
            emailService.sendOtpEmail(request.getEmail(), otpModel.getCode());

            response.setSuccess(userMapper.userModelToRegisterResponse(token));
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    public BaseResponse<Boolean> verifyOtp(VerifyOtpRequest request) {
        BaseResponse<Boolean> response = new BaseResponse<>();

        Optional<UserModel> userModel = userRepository.getUserBySecureId(CurrentUser.get().getUserSecureId());

        if (otpRepository.getUnverifiedOtp(CurrentUser.get().getUserSecureId()).isEmpty() || userModel.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        try {
            Optional<OtpModel> otpModel = otpRepository.getOtp(userModel.get().getSecureId());
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
            settingRepository.save(new SettingModel(userModel.get().getSecureId()));
            tokenModel.ifPresent(tokenRepository::delete);

            historyService.addAccountHistory(REGISTER, userModel.get().getSecureId());

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    public BaseResponse<Boolean> resendOtp() {
        BaseResponse<Boolean> response = new BaseResponse<>();

        Optional<TokenModel> tokenModel = tokenRepository.findByTokenAndTemporaryIsTrue(CurrentUser.get().getToken());
        Optional<OtpModel> otpModel = otpRepository.getOtp(CurrentUser.get().getUserSecureId());
        Optional<UserModel> userModel = userRepository.getUserBySecureId(CurrentUser.get().getUserSecureId());
        Optional<ProfileModel> profileModel = profileRepository.getProfileBySecureId(CurrentUser.get().getUserSecureId());

        if (otpRepository.getUnverifiedOtp(CurrentUser.get().getUserSecureId()).isEmpty() || profileModel.isEmpty() || tokenModel.isEmpty() || otpModel.isEmpty() || userModel.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        try {
            String newOtp = generateOTP();
            otpModel.get().setCode(newOtp);
            tokenModel.get().setExpireAt(getTemporaryDate());

            otpRepository.save(otpModel.get());
            tokenRepository.save(tokenModel.get());
            emailService.sendOtpEmail(profileModel.get().getEmail(), newOtp);

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    public BaseResponse<Boolean> logout() {
        BaseResponse<Boolean> response = new BaseResponse<>();

        Optional<TokenModel> tokenModel = tokenRepository.findByToken(CurrentUser.get().getToken());

        if (tokenModel.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        try {
            tokenModel.ifPresent(tokenRepository::delete);
            historyService.addAccountHistory(LOGOUT, null);

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    public BaseResponse<Boolean> forgotPassword(ForgotPasswordRequest request) {
        BaseResponse<Boolean> response = new BaseResponse<>();

        Optional<ProfileModel> profileModel = profileRepository.getProfileByEmail(request.getEmail());
        Optional<UserModel> userModel = userRepository.getActiveUserBySecureId(profileModel.map(ProfileModel::getUser).orElse(null));

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

    public BaseResponse<Boolean> resetPassword(ResetPasswordRequest request) {
        BaseResponse<Boolean> response = new BaseResponse<>();

        List<ResetModel> resetModelList = resetRepository.getReset(request.getCode());
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

        Optional<UserModel> userModel = userRepository.getActiveUserBySecureId(CurrentUser.get().getSecureId());

        if (userModel.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        try {
            userRepository.save(userMapper.resetPasswordRequestToUserModel(userModel.get(), request.getNewPassword()));

            resetModel.setVerified(true);
            resetRepository.save(resetModel);

            historyService.addAccountHistory(RESET_PASSWORD, userModel.get().getSecureId());

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }

    public BaseResponse<Boolean> changePassword(ChangePasswordRequest request) {
        BaseResponse<Boolean> response = new BaseResponse<>();

        Optional<UserModel> model = userRepository.getUserBySecureId(CurrentUser.get().getUserSecureId());

        if (model.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        if (!Objects.equals(encryptor(model.get().getPassword(), false), request.getOldPassword())) {
            throw new AppException(AUTH_ERROR_MESSAGE);
        }

        try {
            userRepository.save(userMapper.changePasswordRequestToUserModel(request));

            historyService.addAccountHistory(CHANGE_PASSWORD, null);

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }
}
