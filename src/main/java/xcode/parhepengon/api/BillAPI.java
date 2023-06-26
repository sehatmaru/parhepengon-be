package xcode.parhepengon.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.parhepengon.domain.enums.BillTypeEnum;
import xcode.parhepengon.domain.enums.SplitTypeEnum;
import xcode.parhepengon.domain.request.BaseRequest;
import xcode.parhepengon.domain.request.bill.CreateBillRequest;
import xcode.parhepengon.domain.request.comment.AddCommentRequest;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.domain.response.SecureIdResponse;
import xcode.parhepengon.domain.response.bill.BillDetailResponse;
import xcode.parhepengon.domain.response.bill.BillResponse;
import xcode.parhepengon.service.BillService;
import xcode.parhepengon.service.HistoryService;

import java.util.List;

@Validated
@RestController
@RequestMapping(value = "bill")
public class BillAPI {
    
    @Autowired private BillService billService;
    @Autowired private HistoryService historyService;

    @PostMapping("/create")
    ResponseEntity<BaseResponse<SecureIdResponse>> create(@RequestBody @Validated CreateBillRequest request) {
        BaseResponse<SecureIdResponse> response = billService.create(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/update")
    ResponseEntity<BaseResponse<Boolean>> update(@RequestBody @Validated CreateBillRequest request) {
        BaseResponse<Boolean> response = billService.edit(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/delete")
    ResponseEntity<BaseResponse<Boolean>> delete(@RequestBody @Validated BaseRequest request) {
        BaseResponse<Boolean> response = billService.delete(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/comment/add")
    ResponseEntity<BaseResponse<SecureIdResponse>> addComment(@RequestBody @Validated AddCommentRequest request) {
        BaseResponse<SecureIdResponse> response = historyService.addComment(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/detail")
    ResponseEntity<BaseResponse<BillDetailResponse>> detail(@RequestBody @Validated BaseRequest request) {
        BaseResponse<BillDetailResponse> response = billService.detail(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/list")
    ResponseEntity<BaseResponse<List<BillResponse>>> list() {
        BaseResponse<List<BillResponse>> response = billService.list();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("category/list")
    ResponseEntity<BaseResponse<List<BillTypeEnum>>> getCategoryList() {
        BaseResponse<List<BillTypeEnum>> response = billService.getCategoryList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("method/list")
    ResponseEntity<BaseResponse<List<SplitTypeEnum>>> getMethodList() {
        BaseResponse<List<SplitTypeEnum>> response = billService.getMethodList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
