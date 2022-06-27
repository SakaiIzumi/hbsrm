package net.bncloud.saas.supplier.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bncloud.saas.tenant.service.dto.UserDetailRefDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailDTO {

    private UserInfDTO userInfo;

    private List<UserDetailRefDTO> orgInfo;

}
