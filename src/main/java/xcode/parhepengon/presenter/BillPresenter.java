package xcode.parhepengon.presenter;

import xcode.parhepengon.domain.request.BaseRequest;
import xcode.parhepengon.domain.request.bill.CreateBillRequest;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.domain.response.SecureIdResponse;
import xcode.parhepengon.domain.response.bill.BillDetailResponse;
import xcode.parhepengon.domain.response.bill.BillResponse;

import java.util.List;

public interface BillPresenter {
    BaseResponse<SecureIdResponse> create(CreateBillRequest request);
    BaseResponse<Boolean> edit(CreateBillRequest request);
    BaseResponse<Boolean> delete(BaseRequest request);
    BaseResponse<BillDetailResponse> detail(BaseRequest request);
    BaseResponse<List<BillResponse>> list();
}
