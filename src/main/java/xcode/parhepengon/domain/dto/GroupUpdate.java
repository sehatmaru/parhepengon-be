package xcode.parhepengon.domain.dto;

import lombok.Getter;
import lombok.Setter;
import xcode.parhepengon.domain.enums.GroupHistoryEventEnum;
import xcode.parhepengon.domain.enums.GroupTypeEnum;

@Setter
@Getter
public class GroupUpdate {
    private String fullName;
    private String groupSecureId;
    private GroupHistoryEventEnum event;
    private String addedUserName;
    private String deletedUserName;
    private String oldOwner;
    private String newOwner;
    private String oldName;
    private String newName;
    private GroupTypeEnum oldCategory;
    private GroupTypeEnum newCategory;
}
