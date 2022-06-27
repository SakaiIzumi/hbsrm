package net.bncloud.saas.tenant.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BatchZltPurchaserStaffVO implements Serializable {

    List<ZltPurchaserStaffVO> purchaserUsers;

    private List<Long> roleIds;
}
