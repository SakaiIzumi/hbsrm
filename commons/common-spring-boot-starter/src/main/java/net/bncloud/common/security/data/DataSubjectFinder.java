package net.bncloud.common.security.data;

import java.util.List;

/**
 * @author 武书静 wusj4 shujing.wu@meicloud.com
 */
public interface DataSubjectFinder {
    List<DataSubject> find(String appCode, String key);
}
