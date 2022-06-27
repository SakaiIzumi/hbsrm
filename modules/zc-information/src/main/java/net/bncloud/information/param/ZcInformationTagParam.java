package net.bncloud.information.param;

import net.bncloud.information.entity.ZcInformationRoute;
import net.bncloud.information.entity.ZcInformationTag;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.List;

/**
 * 智采消息标签-查询传参包装类
 * @author dr
 */
@Data
public class ZcInformationTagParam extends ZcInformationTag implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<ZcInformationRoute> routeList;
}
