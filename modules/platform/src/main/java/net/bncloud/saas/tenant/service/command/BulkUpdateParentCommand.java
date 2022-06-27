package net.bncloud.saas.tenant.service.command;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BulkUpdateParentCommand {

    private Long parentId;
    private List<Long> deptIds;
}
