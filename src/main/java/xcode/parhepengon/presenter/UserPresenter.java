package xcode.parhepengon.presenter;

import xcode.parhepengon.domain.request.auth.*;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.domain.response.auth.LoginResponse;
import xcode.parhepengon.domain.response.auth.RegisterResponse;

public interface UserPresenter {
    BaseResponse<LoginResponse> login(LoginRequest request);
    BaseResponse<RegisterResponse> register(RegisterRequest request);
    BaseResponse<Boolean> verifyOtp(VerifyOtpRequest request);
    BaseResponse<Boolean> resendOtp();
    BaseResponse<Boolean> changePassword(ChangePasswordRequest request);
    BaseResponse<Boolean> logout();
    BaseResponse<Boolean> forgotPassword(ForgotPasswordRequest request);
    BaseResponse<Boolean> resetPassword(ResetPasswordRequest request);
}
