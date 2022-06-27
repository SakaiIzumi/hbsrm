package net.bncloud.saas.authorize.service.dto;

import java.io.Serializable;

public class MenuMetaVo implements Serializable {

    private static final long serialVersionUID = 7694981774377147937L;
    private String title;

    private String icon;

    public MenuMetaVo() {
    }

    public MenuMetaVo(String title, String icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
