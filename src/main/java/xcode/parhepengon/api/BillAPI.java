package xcode.parhepengon.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.parhepengon.domain.request.BaseRequest;
import xcode.parhepengon.domain.request.bill.CreateBillRequest;
import xcode.parhepengon.domain.request.comment.AddCommentRequest;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.domain.response.SecureIdResponse;
import xcode.parhepengon.domain.response.bill.BillDetailResponse;
import xcode.parhepengon.domain.response.bill.BillResponse;
import xcode.parhepengon.presenter.BillPresenter;
import xcode.parhepengon.presenter.HistoryPresenter;

import java.util.List;

@Validated
@RestController
@RequestMapping(value = "bill")
public class BillAPI {
    
    final BillPresenter billPresenter;
    final HistoryPresenter historyPresenter;

    public BillAPI(BillPresenter billPresenter, HistoryPresenter historyPresenter) {
        this.billPresenter = billPresenter;
        this.historyPresenter = historyPresenter;
    }

    @PostMapping("/create")
    ResponseEntity<BaseResponse<SecureIdResponse>> create(@RequestBody @Validated CreateBillRequest request) {
        BaseResponse<SecureIdResponse> response = billPresenter.create(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/update")
    ResponseEntity<BaseResponse<Boolean>> update(@RequestBody @Validated CreateBillRequest request) {
        BaseResponse<Boolean> response = billPresenter.edit(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/delete")
    ResponseEntity<BaseResponse<Boolean>> delete(@RequestBody @Validated BaseRequest request) {
        BaseResponse<Boolean> response = billPresenter.delete(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/comment/add")
    ResponseEntity<BaseResponse<SecureIdResponse>> addHistory(@RequestBody @Validated AddCommentRequest request) {
        BaseResponse<SecureIdResponse> response = historyPresenter.addComment(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/detail")
    ResponseEntity<BaseResponse<BillDetailResponse>> detail(@RequestBody @Validated BaseRequest request) {
        BaseResponse<BillDetailResponse> response = billPresenter.detail(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/list")
    ResponseEntity<BaseResponse<List<BillResponse>>> list() {
        BaseResponse<List<BillResponse>> response = billPresenter.list();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
