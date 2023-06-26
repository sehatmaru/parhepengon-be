package xcode.parhepengon.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xcode.parhepengon.domain.request.profile.EditProfileRequest;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.service.ProfileService;

@Validated
@RestController
@RequestMapping(value = "profile")
public class ProfileAPI {
    
    @Autowired private ProfileService profileService;

    @PostMapping("/update")
    ResponseEntity<BaseResponse<Boolean>> update(@RequestBody @Validated EditProfileRequest request) {
        BaseResponse<Boolean> response = profileService.update(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
