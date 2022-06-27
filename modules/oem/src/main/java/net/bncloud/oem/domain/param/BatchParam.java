package net.bncloud.oem.domain.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liyh
 * @description 批量操作参数
 * @since 2022/5/5
 */
@Data
public class BatchParam {
    List<String> ids=new ArrayList<>();
}
