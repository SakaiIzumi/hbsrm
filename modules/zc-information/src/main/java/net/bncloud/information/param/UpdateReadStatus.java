package net.bncloud.information.param;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateReadStatus {
    private Integer type;
    private  String ids;
}
