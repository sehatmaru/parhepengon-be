package xcode.parhepengon.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class Bill {

    private String member;

    private BigDecimal amount;

    private BigDecimal amountPercentage;
}
