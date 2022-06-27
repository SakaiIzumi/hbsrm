package net.bncloud.saas.supplier.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TypeConfigItemDTO implements Serializable {
    private static final long serialVersionUID = 481069490055506397L;

    private Long id;

    private String item;

    private TypeDTO type;
}
