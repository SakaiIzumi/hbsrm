package net.bncloud.saas.supplier.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class TagDTO implements Serializable {
    private static final long serialVersionUID = -1764339702031362860L;

    private Long id;

    private Long orgId;

    private String group;

    private List<String> tags = new ArrayList<>();
}
