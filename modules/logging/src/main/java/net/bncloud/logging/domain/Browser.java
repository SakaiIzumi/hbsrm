package net.bncloud.logging.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Browser {

    private String browserName;
    private String browserEngine;

    /** 浏览器版本 */
    private String browserVersion;
    /** 引擎版本 */
    private String browserEngineVersion;
}
