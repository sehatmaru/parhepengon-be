package xcode.parhepengon.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.parhepengon.domain.enums.GroupTypeEnum;
import xcode.parhepengon.domain.request.BaseRequest;
import xcode.parhepengon.domain.request.group.AddKickMemberRequest;
import xcode.parhepengon.domain.request.group.CreateGroupRequest;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.domain.response.SecureIdResponse;
import xcode.parhepengon.domain.response.auth.UserResponse;
import xcode.parhepengon.domain.response.group.GroupDetailResponse;
import xcode.parhepengon.domain.response.group.GroupResponse;
import xcode.parhepengon.domain.response.group.MemberResponse;
import xcode.parhepengon.service.GroupMemberService;
import xcode.parhepengon.service.GroupService;

import java.util.List;

@Validated
@RestController
@RequestMapping(value = "group")
public class GroupAPI {
    
    @Autowired private GroupService groupService;

    @Autowired private GroupMemberService groupMemberService;

    @PostMapping("/create")
    ResponseEntity<BaseResponse<SecureIdResponse>> create(@RequestBody @Validated CreateGroupRequest request) {
        BaseResponse<SecureIdResponse> response = groupService.create(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/update")
    ResponseEntity<BaseResponse<Boolean>> update(@RequestBody @Validated CreateGroupRequest request) {
        BaseResponse<Boolean> response = groupService.edit(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/delete")
    ResponseEntity<BaseResponse<Boolean>> update(@RequestBody @Validated BaseRequest request) {
        BaseResponse<Boolean> response = groupService.delete(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/member/add")
    ResponseEntity<BaseResponse<Boolean>> addMember(@RequestBody @Validated AddKickMemberRequest request) {
        BaseResponse<Boolean> response = groupMemberService.add(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/member/kick")
    ResponseEntity<BaseResponse<Boolean>> kickMember(@RequestBody @Validated AddKickMemberRequest request) {
        BaseResponse<Boolean> response = groupMemberService.kick(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/member/list")
    ResponseEntity<BaseResponse<List<MemberResponse>>> memberList(@RequestBody @Validated BaseRequest request) {
        BaseResponse<List<MemberResponse>> response = groupService.getMemberList(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/non-member/list")
    ResponseEntity<BaseResponse<List<UserResponse>>> nonMemberList(@RequestBody @Validated BaseRequest request) {
        BaseResponse<List<UserResponse>> response = groupService.getNonMemberList(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/member/leave")
    ResponseEntity<BaseResponse<Boolean>> leave(@RequestBody @Validated BaseRequest request) {
        BaseResponse<Boolean> response = groupMemberService.leave(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/list")
    ResponseEntity<BaseResponse<List<GroupResponse>>> getList() {
        BaseResponse<List<GroupResponse>> response = groupService.getList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/detail")
    ResponseEntity<BaseResponse<GroupDetailResponse>> detail(@RequestBody @Validated BaseRequest request) {
        BaseResponse<GroupDetailResponse> response = groupService.detail(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("category/list")
    ResponseEntity<BaseResponse<List<GroupTypeEnum>>> getCategoryList() {
        BaseResponse<List<GroupTypeEnum>> response = groupService.getCategoryList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
