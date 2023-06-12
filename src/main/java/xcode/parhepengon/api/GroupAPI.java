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
import xcode.parhepengon.domain.request.group.AddKickMemberRequest;
import xcode.parhepengon.domain.request.group.CreateGroupRequest;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.domain.response.SecureIdResponse;
import xcode.parhepengon.presenter.GroupMemberPresenter;
import xcode.parhepengon.presenter.GroupPresenter;

@Validated
@RestController
@RequestMapping(value = "group")
public class GroupAPI {
    
    final GroupPresenter groupPresenter;

    final GroupMemberPresenter groupMemberPresenter;

    public GroupAPI(GroupPresenter groupPresenter, GroupMemberPresenter groupMemberPresenter) {
        this.groupPresenter = groupPresenter;
        this.groupMemberPresenter = groupMemberPresenter;
    }

    @PostMapping("/create")
    ResponseEntity<BaseResponse<SecureIdResponse>> create(@RequestBody @Validated CreateGroupRequest request) {
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

    @PostMapping("/member/add")
    ResponseEntity<BaseResponse<Boolean>> addMember(@RequestBody @Validated AddKickMemberRequest request) {
        BaseResponse<Boolean> response = groupMemberPresenter.add(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/member/kick")
    ResponseEntity<BaseResponse<Boolean>> kickMember(@RequestBody @Validated AddKickMemberRequest request) {
        BaseResponse<Boolean> response = groupMemberPresenter.kick(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
