package net.bncloud.information.vo;

import net.bncloud.information.entity.ZcInformationMsg;
import lombok.Data;
import net.bncloud.information.entity.ZcInformationRoute;

import java.io.Serializable;
import java.util.List;

/**
 * 智采消息表
 * @author dr
 */
@Data
public class ZcInformationMsgVo extends ZcInformationMsg implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 标签名称
     */
    private String tagName;
    private Long tagId;

    private List<ZcInformationRoute> routeList;



}