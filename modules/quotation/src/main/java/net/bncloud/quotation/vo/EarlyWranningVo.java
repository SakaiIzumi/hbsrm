package net.bncloud.quotation.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EarlyWranningVo implements Serializable {
    /**
     * 开关
     */
    private String supplierWarningSwitch;
    /**
     * 报价截止时间
     */
    private Date cutOffTime;
    /**
     * 协同配置设置的时间
     */
    private Long nearTime;
    /**
     * 发送的消息类型：1 站内信 2 短信
     */
    private List<Integer> type=new ArrayList<>();
    /**
     * 发送的消息类型（中文，前端渲染）：1 站内信 2 短信
     */
    private List<String> typeString=new ArrayList<>();

}
