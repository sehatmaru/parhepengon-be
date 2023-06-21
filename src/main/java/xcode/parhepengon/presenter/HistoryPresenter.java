package xcode.parhepengon.presenter;

import xcode.parhepengon.domain.request.comment.AddCommentRequest;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.domain.response.SecureIdResponse;

public interface HistoryPresenter {
    BaseResponse<SecureIdResponse> addComment(AddCommentRequest request);
}
