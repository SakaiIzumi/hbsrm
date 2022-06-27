package net.bncloud.saas.tenant.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailDTO {

    private UserInfDTO userInfo;

    private List<UserDetailRefDTO> orgInfo;

}
