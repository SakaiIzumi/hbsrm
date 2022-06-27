package net.bncloud.security.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.Role;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.security.data.*;
import net.bncloud.common.util.ApplicationContextProvider;
import net.bncloud.common.web.util.RequestUtils;
import net.bncloud.security.data.config.DataSecurityProperties;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@AllArgsConstructor
@Slf4j
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> implements ContextualSerializer {


    public final static BigDecimalSerializer instance = new BigDecimalSerializer();

    private boolean desensitize = false;

    public BigDecimalSerializer() {
    }

    public boolean isDesensitize() {
        return desensitize;
    }

    public void setDesensitize(boolean desensitize) {
        this.desensitize = desensitize;
    }

    @Override
    public void serialize(BigDecimal value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        GrantDataHolder grantDataHolder = ApplicationContextProvider.getBean(GrantDataHolder.class);
        DataSubjectHolder dataSubjectHolder = ApplicationContextProvider.getBean(DataSubjectHolder.class);
        DesensitizeFieldHolder desensitizeFieldHolder = ApplicationContextProvider.getBean(DesensitizeFieldHolder.class);
        Environment env = ApplicationContextProvider.getBean(Environment.class);
        String appCode = env.getProperty("bncloud.security.data.app-code");
        String urlId = RequestUtils.getHttpServletRequest().getRequestURI();
        // 数据主题
        List<DataSubject> dataSubjects = dataSubjectHolder.get(appCode, urlId);
        LoginInfo loginInfo = SecurityUtils.getLoginInfo().orElseThrow(() -> new ApiException(401, "用户未登陆或授权失败"));
        Set<Role> roles = loginInfo.getRoles();
        if (roles == null || roles.isEmpty()) {
            writerSerialize(value, jsonGenerator);
            return;
        }
        if (desensitize) {
            if (dataSubjects == null || dataSubjects.isEmpty()) {
                writerSerialize(value, jsonGenerator);
                return;
            }
            for (DataSubject dataSubject : dataSubjects) {
                String apiValue = dataSubject.getKey();
                if (!urlId.equals(apiValue)) {
                    writerSerialize(value, jsonGenerator);
                    return;
                }
                List<DataDimension> dimensions = dataSubject.getDimensions();
                if (dimensions == null || dimensions.isEmpty()) {
                    writerSerialize(value, jsonGenerator);
                    return;
                }


                for (DataDimension dimension : dimensions) {
                    Set<DataDimensionGrant> grantSet = new HashSet<>();
                    for (Role role : roles) {
                        DataDimensionGrant dataDimensionGrant = grantDataHolder.get(role.getId().toString(), dataSubject.getId(), dimension.getCode());
                        if (grantSet.contains(dataDimensionGrant)) {
                            continue;
                        }
                        grantSet.add(dataDimensionGrant);
                        if (dataDimensionGrant != null) {
                            //维度
                            String dimensionCode = dataDimensionGrant.getDimensionCode();
                            DataDesensitizeField dataDesensitizeField = desensitizeFieldHolder.get(appCode, dataSubject.getId(), dimensionCode);
                            if (dataDesensitizeField != null) {
                                String pattern = dataDesensitizeField.getPattern();
                                String result = value.toString();
                                result = result.replaceAll(result, pattern);
                                jsonGenerator.writeString(result);
                                return;
                            }
                        }
                    }
                }
            }
        }
        writerSerialize(value, jsonGenerator);

    }


    private void writerSerialize(BigDecimal value, JsonGenerator jsonGenerator) throws IOException {
        if (value != null) {
            BigDecimal bigDecimal = value.setScale(2, RoundingMode.HALF_UP);
            jsonGenerator.writeNumber(bigDecimal);
        } else {
            BigDecimal bigDecimal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            jsonGenerator.writeNumber(bigDecimal);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        DesensitizeFieldHolder desensitizeFieldHolder = ApplicationContextProvider.getBean(DesensitizeFieldHolder.class);
        DataSecurityProperties prop = ApplicationContextProvider.getBean(DataSecurityProperties.class);
        if (prop == null || prop.getAppCode() == null) {
            log.error("配置异常,请检查配置是否合法");
        }
        String name = beanProperty.getName();
        BigDecimalSerializer bigDecimalSerializer = new BigDecimalSerializer();
        Map<String, List<DataDesensitizeField>> stringListMap = desensitizeFieldHolder.get(prop.getAppCode());
        List<DataDesensitizeField> all = new ArrayList<>();
        Set<String> keys = stringListMap.keySet();
        keys.forEach(key -> {
            all.addAll(stringListMap.get(key));
        });
        for (DataDesensitizeField dataDesensitizeField : all) {
            String dimensionCode = dataDesensitizeField.getDimensionCode();
            if (name.equals(dimensionCode)) {
                bigDecimalSerializer.setDesensitize(true);
            }
        }
        return bigDecimalSerializer;
    }
}
