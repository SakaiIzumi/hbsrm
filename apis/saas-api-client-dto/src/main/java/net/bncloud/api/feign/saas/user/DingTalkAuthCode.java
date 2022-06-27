package net.bncloud.api.feign.saas.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DingTalkAuthCode {
    private String corpId;
    private Long agentId;
    private String code;

    public static DingTalkAuthCode of(String corpId, Long agentId, String code) {
        return new DingTalkAuthCode(corpId, agentId, code);
    }

    @Override
    public String toString() {
        return "DingTalkAuthCode{" +
                "corpId='" + corpId + '\'' +
                ", agentId=" + agentId +
                ", code='" + code + '\'' +
                '}';
    }
}
