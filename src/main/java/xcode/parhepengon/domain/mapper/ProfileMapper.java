package xcode.parhepengon.domain.mapper;

import xcode.parhepengon.domain.model.ProfileModel;
import xcode.parhepengon.domain.request.auth.RegisterRequest;
import xcode.parhepengon.domain.request.profile.EditProfileRequest;

import java.util.Date;

import static xcode.parhepengon.shared.Utils.generateSecureId;

public class ProfileMapper {
    public ProfileModel editModel(ProfileModel model, EditProfileRequest request) {
        if (model != null && request != null) {
            model.setPhone(request.getPhone());
            model.setEmail(request.getEmail());
            model.setFullName(request.getFullName());
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public ProfileModel registerRequestToProfileModel(RegisterRequest request, String user) {
        if (request != null) {
            ProfileModel model = new ProfileModel();
            model.setSecureId(generateSecureId());
            model.setUser(user);
            model.setEmail(request.getEmail());
            model.setPhone(request.getPhone());
            model.setFullName(request.getFullName());
            model.setCreatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

}
