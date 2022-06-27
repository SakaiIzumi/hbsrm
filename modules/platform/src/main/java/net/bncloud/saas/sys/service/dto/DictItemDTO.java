package net.bncloud.saas.sys.service.dto;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.convert.base.BaseDTO;

import java.io.Serializable;


public class DictItemDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = -2721635846058398095L;
    private Long id;

    private DictSmallDto dict;

    private String label;

    private String value;

    private int order;

    private String extJson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DictSmallDto getDict() {
        return dict;
    }

    public void setDict(DictSmallDto dict) {
        this.dict = dict;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getExtJson() {
        return extJson;
    }

    public void setExtJson(String extJson) {
        this.extJson = extJson;
    }
}
