package net.bncloud.saas.sys.service.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import net.bncloud.convert.base.BaseDTO;

import java.io.Serializable;
import java.time.Instant;

public class SupplierOpsLogDto extends BaseDTO implements Serializable{

    private static final long serialVersionUID = -792376893069324200L;

    private Long id;

    private Long supplierId;

    private String content;

    private String remark;

    private Long createdBy;

    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private Instant createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
}
