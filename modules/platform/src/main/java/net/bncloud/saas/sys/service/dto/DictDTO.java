package net.bncloud.saas.sys.service.dto;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.convert.base.BaseDTO;

import java.io.Serializable;
import java.util.List;

public class DictDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = -2545285470905246893L;

    private List<DictItemDTO> items;

    private String code;

    private String description;

    private String extJson;

    public List<DictItemDTO> getItems() {
        return items;
    }

    public void setItems(List<DictItemDTO> items) {
        this.items = items;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtJson() {
        return extJson;
    }

    public void setExtJson(String extJson) {
        this.extJson = extJson;
    }
}
