package xcode.parhepengon.presenter;

import xcode.parhepengon.domain.model.UserModel;

public interface JwtPresenter {
    String generateToken(UserModel user);
}
