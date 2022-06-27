package net.bncloud.saas.tenant.web.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrgEmployeeMangeScopeListParam implements Serializable {

   List<OrgEmployeeMangeScopeParam> orgEmployeeMangeScopeParamList =new ArrayList<>();
}
