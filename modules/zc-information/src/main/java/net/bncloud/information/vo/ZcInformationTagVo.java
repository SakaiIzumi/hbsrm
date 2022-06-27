package net.bncloud.information.vo;

import net.bncloud.information.entity.ZcInformationRoute;
import net.bncloud.information.entity.ZcInformationTag;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.List;

/**
 * 智采消息标签-返回增强实体包装类
 * @author dr
 */
@Data
public class ZcInformationTagVo extends ZcInformationTag implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<ZcInformationRoute> routeList;
}
