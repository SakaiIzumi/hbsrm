package net.bncloud.saas.ding.domain.pk;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class DingTalkCorpManagerPK implements Serializable {

    private static final long serialVersionUID = -9181782765226199999L;
    @Column(name = "corp_id")
    private String corpId;
    @Column(name = "user_id")
    private String userId;
}
