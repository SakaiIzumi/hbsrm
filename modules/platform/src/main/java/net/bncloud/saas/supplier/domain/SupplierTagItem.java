package net.bncloud.saas.supplier.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "t_supplier_tag")
@Embeddable
@Getter
@Setter

public class SupplierTagItem {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tagId;
    private String tags;





}
