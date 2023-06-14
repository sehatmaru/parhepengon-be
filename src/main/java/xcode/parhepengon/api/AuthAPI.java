package xcode.parhepengon.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.parhepengon.domain.request.auth.*;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.domain.response.auth.LoginResponse;
import xcode.parhepengon.domain.response.auth.RegisterResponse;
import xcode.parhepengon.presenter.UserPresenter;

@Validated
@RestController
@RequestMapping(value = "auth")
public class AuthAPI {
    
    final UserPresenter userPresenter;

    public AuthAPI(UserPresenter userPresenter) {
        this.userPresenter = userPresenter;
    }

    @PostMapping("/login")
    ResponseEntity<BaseResponse<LoginResponse>> login(@RequestBody @Validated LoginRequest request) {
        BaseResponse<LoginResponse> response = userPresenter.login(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/register")
    ResponseEntity<BaseResponse<RegisterResponse>> register(@RequestBody @Validated RegisterRequest request) {
        BaseResponse<RegisterResponse> response = userPresenter.register(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/change-password")
    ResponseEntity<BaseResponse<Boolean>> changePassword(@RequestBody @Validated ChangePasswordRequest request) {
        BaseResponse<Boolean> response = userPresenter.changePassword(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/logout")
    ResponseEntity<BaseResponse<Boolean>> logout() {
        BaseResponse<Boolean> response = userPresenter.logout();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/otp/verify")
    ResponseEntity<BaseResponse<Boolean>> verifyOtp(@RequestBody @Validated VerifyOtpRequest request) {
        BaseResponse<Boolean> response = userPresenter.verifyOtp(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/password/forgot")
    ResponseEntity<BaseResponse<Boolean>> forgotPassword(@RequestBody @Validated ForgotPasswordRequest request) {
        BaseResponse<Boolean> response = userPresenter.forgotPassword(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/password/reset")
    ResponseEntity<BaseResponse<Boolean>> resetPassword(@RequestBody @Validated ResetPasswordRequest request) {
        BaseResponse<Boolean> response = userPresenter.resetPassword(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/otp/resend")
    ResponseEntity<BaseResponse<Boolean>> resendOtp() {
        BaseResponse<Boolean> response = userPresenter.resendOtp();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
