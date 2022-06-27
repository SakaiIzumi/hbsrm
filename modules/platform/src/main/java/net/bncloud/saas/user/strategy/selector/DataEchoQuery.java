package net.bncloud.saas.user.strategy.selector;

import lombok.Data;

import java.util.List;

@Data
public class DataEchoQuery {

    private List<String> ids;

    private String type;
}
