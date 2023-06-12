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
import xcode.parhepengon.domain.request.bill.CreateBillRequest;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.domain.response.SecureIdResponse;
import xcode.parhepengon.presenter.BillPresenter;

@Validated
@RestController
@RequestMapping(value = "bill")
public class BillAPI {
    
    final BillPresenter billPresenter;

    public BillAPI(BillPresenter billPresenter) {
        this.billPresenter = billPresenter;
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
    ResponseEntity<BaseResponse<Boolean>> update(@RequestBody @Validated BaseRequest request) {
        BaseResponse<Boolean> response = billPresenter.delete(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
