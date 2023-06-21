package xcode.parhepengon.domain.response.group;

import lombok.Getter;
import lombok.Setter;
import xcode.parhepengon.domain.enums.GroupTypeEnum;

import java.math.BigDecimal;

@Getter
@Setter
public class GroupResponse {
    private String secureId;
    private String name;
    private GroupTypeEnum category;
    private BigDecimal totalBalance;
    private boolean funder;
}
