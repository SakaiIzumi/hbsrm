package net.bncloud.saas.event.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.saas.event.CreateSupplierOpsLog;
import net.bncloud.saas.event.SupplierOpsLogCreatedEvent;
import net.bncloud.saas.supplier.domain.SupplierOpsLog;
import net.bncloud.saas.supplier.service.SupplierOpsLogService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class SupplierOpsLogEventListener {

    private final SupplierOpsLogService supplierOpsLogService;

    @EventListener(SupplierOpsLogCreatedEvent.class)
    public void listenerSupplierOpsLogCreatedEvent(SupplierOpsLogCreatedEvent supplierOpsLogCreatedEvent) {
        CreateSupplierOpsLog createSupplierOpsLog = supplierOpsLogCreatedEvent.getCreateSupplierOpsLog();
        SupplierOpsLog opsLog = SupplierOpsLog.builder()
                .supplierId(createSupplierOpsLog.getSupplierId())
                .content(createSupplierOpsLog.getContent())
                .remark(createSupplierOpsLog.getRemark())
                .opsUserName(createSupplierOpsLog.getOpsUserName())
                .build();
        supplierOpsLogService.save(opsLog);
    }
}
