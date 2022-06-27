package net.bncloud.saas.supplier.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class TypeDTO implements Serializable {

    private static final long serialVersionUID = -5250588194536528155L;
    private Long id;

    private Long orgId;

    private String group;

    private List<String> types = new ArrayList<>();

}
