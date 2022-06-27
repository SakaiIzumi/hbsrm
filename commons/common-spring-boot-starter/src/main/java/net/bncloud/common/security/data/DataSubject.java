package net.bncloud.common.security.data;

import java.util.List;
import java.util.Objects;

/**
 * 数据主题
 * @author 武书静 wusj4 shujing.wu@meicloud.com
 */
public class DataSubject {

    private String id;

    private String key;

    private List<DataDimension> dimensions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<DataDimension> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<DataDimension> dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSubject that = (DataSubject) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
