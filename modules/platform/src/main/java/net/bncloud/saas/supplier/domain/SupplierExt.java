package net.bncloud.saas.supplier.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import net.bncloud.api.feign.saas.org.OrgDTO;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.*;

@Entity
@Table(name = "t_supplier_info_ext")
@Getter
@Setter
public class SupplierExt extends AbstractAuditingEntity {
    private static final long serialVersionUID = 2261806723244229264L;
    @Id
    private Long supplierId;

    @Column(name = "supplier_account", columnDefinition = "varchar(100) comment '智采帐号'")
    private String supplierAccount;

    @Column(name = "supplier_nick_name", columnDefinition = "varchar(100) comment '供应商简称'")
    private String supplierNickName;

    @Column(name = "bank_outlet", columnDefinition = "varchar(100) comment '开户银行网点'")
    private String bankOutlet;

    @Column(name = "bank_deposit", columnDefinition = "varchar(100) comment '开户行'")
    private String bankDeposit;

    @Column(name = "bank_account", columnDefinition = "varchar(100) comment '银行账号'")
    private String bankAccount;

    @Column(name = "bank_account_name", columnDefinition = "varchar(100) comment '银行账户名称'")
    private String bankAccountName;

    @Column(name = "taxpayer_no", columnDefinition = "varchar(255) comment '纳税人识别号/纳税登记号'")
    private String taxpayerNo;

    @Column(name = "payment_clause", columnDefinition = "varchar(255) comment '付款条件'")
    private String paymentClause;

    @Column(name = "bourse_currency", columnDefinition = "varchar(255) comment '交易币别'")
    private String bourseCurrency;

    @Column(name = "terms_exchange", columnDefinition = "varchar(255) comment '交易条件'")
    private String termsExchange;

    @Column(name = "classification_code", columnDefinition = "varchar(255) comment '分类编码'")
    private String classificationCode;


    @Column(name = "purchase_person_id", columnDefinition = "bigint(20) comment '采购员Id'")
    private Long purchasePersonId;

    @Column(name = "company_id", columnDefinition = "bigint(20) comment '公司id'")
    private Long companyId;

    @Column(name = "company_name", columnDefinition = "varchar(100) comment '公司名称'")
    private String companyName;

    @Column(name = "company_nick_name", columnDefinition = "varchar(100) comment '公司简称'")
    private String companyNickName;

    @Column(name = "dept_id", columnDefinition = "bigint(20) comment '部门id'")
    private Long deptId;

    @Column(name = "dept_name", columnDefinition = "varchar(100) comment '部门名称'")
    private String deptName;

    @Column(name = "country", columnDefinition = "varchar(50) comment '国家'")
    private String country;

    @Column(name = "province", columnDefinition = "varchar(50) comment '省份'")
    private String province;

    @Column(name = "city", columnDefinition = "varchar(20) comment '城市'")
    private String city;

    @Column(name = "management_forms", columnDefinition = "varchar(255) comment 'managementForms(未使用)'")
    private String managementForms;


    @JsonIgnore
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", referencedColumnName = "id", nullable = false)
    private Supplier supplier;


}
