package net.bncloud.oem.domain.param;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class AddressCodeAndNameParam {
    private String code;
    private String address;
}
