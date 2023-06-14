package xcode.parhepengon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.parhepengon.domain.mapper.ProfileMapper;
import xcode.parhepengon.domain.model.CurrentUser;
import xcode.parhepengon.domain.model.ProfileModel;
import xcode.parhepengon.domain.model.UserModel;
import xcode.parhepengon.domain.repository.ProfileRepository;
import xcode.parhepengon.domain.repository.UserRepository;
import xcode.parhepengon.domain.request.profile.EditProfileRequest;
import xcode.parhepengon.domain.response.BaseResponse;
import xcode.parhepengon.exception.AppException;
import xcode.parhepengon.presenter.ProfilePresenter;

import java.util.Date;
import java.util.Optional;

import static xcode.parhepengon.domain.enums.EventEnum.UPDATE_PROFILE;
import static xcode.parhepengon.shared.ResponseCode.NOT_FOUND_MESSAGE;

@Service
public class ProfileService implements ProfilePresenter {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    private final ProfileMapper profileMapper = new ProfileMapper();

    @Override
    public BaseResponse<Boolean> update(EditProfileRequest request) {
        BaseResponse<Boolean> response = new BaseResponse<>();

        Optional<ProfileModel> model = profileRepository.getProfileBySecureId(CurrentUser.get().getUserSecureId());
        Optional<UserModel> userModel = userRepository.getActiveUserBySecureId(CurrentUser.get().getUserSecureId());

        if (model.isEmpty() || userModel.isEmpty()) {
            throw new AppException(NOT_FOUND_MESSAGE);
        }

        try {
            profileRepository.save(profileMapper.editModel(model.get(), request));

            userModel.get().setUsername(request.getUsername());
            userModel.get().setUpdatedAt(new Date());
            userRepository.save(userModel.get());

            historyService.addHistory(UPDATE_PROFILE, null);

            response.setSuccess(true);
        } catch (Exception e) {
            throw new AppException(e.toString());
        }

        return response;
    }
}
