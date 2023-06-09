package xcode.parhepengon.domain.mapper;

import xcode.parhepengon.domain.model.OtpModel;
import xcode.parhepengon.domain.model.UserModel;
import xcode.parhepengon.domain.request.auth.ChangePasswordRequest;
import xcode.parhepengon.domain.request.auth.EditProfileRequest;
import xcode.parhepengon.domain.request.auth.RegisterRequest;
import xcode.parhepengon.domain.response.auth.LoginResponse;
import xcode.parhepengon.domain.response.auth.RegisterResponse;

import java.util.Date;

import static xcode.parhepengon.shared.Utils.*;

public class UserMapper {
    public LoginResponse userModelToLoginResponse(UserModel model, String accessToken) {
        if (model != null) {
            LoginResponse response = new LoginResponse();
            response.setSecureId(model.getSecureId());
            response.setFullname(model.getFullname());
            response.setEmail(model.getEmail());
            response.setUsername(model.getUsername());
            response.setAccessToken(accessToken);

            return response;
        } else {
            return null;
        }
    }

    public UserModel registerRequestToUserModel(RegisterRequest request) {
        if (request != null) {
            UserModel model = new UserModel();
            model.setSecureId(generateSecureId());
            model.setFullname(request.getFullname());
            model.setEmail(request.getEmail());
            model.setUsername(request.getUsername());
            model.setPassword(encryptor(request.getPassword(), true));
            model.setCreatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public UserModel editProfileRequestToUserModel(EditProfileRequest request, UserModel model) {
        if (request != null) {
            model.setFullname(request.getFullname());
            model.setEmail(request.getEmail());
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public UserModel changePasswordRequestToUserModel(ChangePasswordRequest request) {
        if (request != null) {
            UserModel model = new UserModel();
            model.setPassword(encryptor(request.getNewPassword(), true));
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public UserModel resetPasswordRequestToUserModel(UserModel model, String newPassword) {
        if (model != null) {
            model.setPassword(encryptor(newPassword, true));
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public RegisterResponse userModelToRegisterResponse(String token) {
        if (!token.isEmpty()) {
            RegisterResponse response = new RegisterResponse();
            response.setTemporaryToken(token);

            return response;
        } else {
            return null;
        }
    }

    public OtpModel userModelToOtpModel(UserModel model) {
        if (model != null) {
            OtpModel response = new OtpModel();
            response.setUserSecureId(model.getSecureId());
            response.setSecureId(generateSecureId());
            response.setCode(generateOTP());
            response.setCreatedAt(new Date());
            response.setExpireAt(getTemporaryDate());

            return response;
        } else {
            return null;
        }
    }

}
