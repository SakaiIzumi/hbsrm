package net.bncloud.saas.user.service.dto;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermissionInfoDTO implements Serializable {

    private List<Map<String, Object>> roles;//角色

    private List<Map<String, Object>> menus;

    private List<Map<String, Object>> perms;

    private List<Map<String, Object>> companies;

    private List<Map<String, Object>> orgs;

    private List<Map<String, Object>> suppliers;

    private List<Map<String, Object>> menuNavs;


    public static PermissionInfoDTO create() {
        return PermissionInfoDTO
                .builder()
                .roles(Lists.newArrayList())
                .menus(Lists.newArrayList())
                .perms(Lists.newArrayList())
                .companies(Lists.newArrayList())
                .orgs(Lists.newArrayList())
                .suppliers(Lists.newArrayList())
                .menuNavs(Lists.newArrayList())
                .build();
    }


}




