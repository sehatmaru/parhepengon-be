package xcode.parhepengon.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Setter
@Getter
public class Bill {

    @NotBlank
    private String member;

    private BigDecimal amount;

    private BigDecimal amountPercentage;
}
