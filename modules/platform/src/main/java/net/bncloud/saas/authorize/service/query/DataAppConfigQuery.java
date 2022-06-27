package net.bncloud.saas.authorize.service.query;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class DataAppConfigQuery {
    private Long id;

    private String appCode;

    private String appName;


}
