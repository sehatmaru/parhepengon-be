package xcode.parhepengon.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xcode.parhepengon.domain.request.BaseRequest;
import xcode.parhepengon.domain.request.group.CreateGroupRequest;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.domain.response.SecureIdResponse;
import xcode.parhepengon.presenter.GroupPresenter;

@Validated
@RestController
@RequestMapping(value = "group")
public class GroupAPI {
    
    final GroupPresenter groupPresenter;

    public GroupAPI(GroupPresenter groupPresenter) {
        this.groupPresenter = groupPresenter;
    }

    @PostMapping("/create")
    ResponseEntity<BaseResponse<SecureIdResponse>> login(@RequestBody @Validated CreateGroupRequest request) {
        BaseResponse<SecureIdResponse> response = groupPresenter.create(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/update")
    ResponseEntity<BaseResponse<Boolean>> update(@RequestBody @Validated CreateGroupRequest request) {
        BaseResponse<Boolean> response = groupPresenter.edit(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/delete")
    ResponseEntity<BaseResponse<Boolean>> update(@RequestBody @Validated BaseRequest request) {
        BaseResponse<Boolean> response = groupPresenter.delete(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
