package net.bncloud.saas.supplier.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Embeddable
@Entity
@Table(name = "t_supplier_type")
@Getter
@Setter
public class SupplierTypeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long typeId;
    private String types;



}
