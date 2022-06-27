package net.bncloud.saas.authorize.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuNodeDTO<T> {

    private Long id;

    private String name;

    private Long parentId;

    private String code;

    private T data;

    List<MenuNodeDTO<T>> children;
}
