package net.bncloud.saas.supplier.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TagConfigItemDTO implements Serializable {
    private static final long serialVersionUID = -2127899210676143929L;

    private Long id;

    private String item;

    private TagDTO tag;
}
