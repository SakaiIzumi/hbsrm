package net.bncloud.saas.supplier.vo;


import lombok.Getter;
import lombok.Setter;

import net.bncloud.saas.supplier.domain.*;

import java.util.Map;


@Getter
@Setter
public class SupplierVo extends Supplier {



    private Map<String,Boolean> permissionButton;
}

