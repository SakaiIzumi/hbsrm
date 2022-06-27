package net.bncloud.saas.supplier.service.command;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ManualSyncCommand implements Serializable {

    private List<Integer> ids;
}
