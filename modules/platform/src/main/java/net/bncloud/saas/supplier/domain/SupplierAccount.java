package net.bncloud.saas.supplier.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "t_supplier_account")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierAccount {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 开户行
     */
    @ApiModelProperty(value = "开户行")
    @Column(name = "bank_outlet", columnDefinition = "varchar(100) comment '开户行'")
    private String bankOutlet;

    /**
     * 开户银行网点
     */
    @ApiModelProperty(value = "开户银行网点")
    @Column(name = "bank_deposit", columnDefinition = "varchar(100) comment '开户银行网点'")
    private String bankDeposit;

    /**
     * 银行账号
     */
    @ApiModelProperty(value = "银行账号")
    @Column(name = "bank_account", columnDefinition = "varchar(100) comment '银行账号'")
    private String bankAccount;

    /**
     * 账户名称
     */
    @ApiModelProperty(value = "账户名称")
    @Column(name = "bank_account_name", columnDefinition = "varchar(100) comment '账户名称'")
    private String bankAccountName;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
}
