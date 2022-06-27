package net.bncloud.common.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.bncloud.common.web.jackson.LongToStringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.ReflectionUtils;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Configuration
public class JacksonConfiguration {

    /**
     * Support for Java date and time API.
     * @return the corresponding Jackson module.
     */
    @Bean
    public JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }

    @Bean
    public Jdk8Module jdk8TimeModule() {
        return new Jdk8Module();
    }

    /*
     * Support for Hibernate types in Jackson.
     */
    @Bean
    @ConditionalOnClass(name = "org.hibernate.engine.spi.Mapping")
    public Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }

    /*
     * Module for serialization/deserialization of RFC7807 Problem.
     */
    @Bean
    ProblemModule problemModule() {
        return new ProblemModule();
    }

    /*
     * Module for serialization/deserialization of ConstraintViolationProblem.
     */
    @Bean
    ConstraintViolationProblemModule constraintViolationProblemModule() {
        return new ConstraintViolationProblemModule();
    }


    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> {
            jacksonObjectMapperBuilder.serializerByType(Long.TYPE, LongToStringSerializer.instance);
            jacksonObjectMapperBuilder.serializerByType(Long.class, LongToStringSerializer.instance);
            jacksonObjectMapperBuilder.serializerByType(long.class, LongToStringSerializer.instance);
        };
    }
    @Bean
    @Primary
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                String fieldName = gen.getOutputContext().getCurrentName();
                // 需要留意的是 此对象是最外层的参数对象
                Object currentValue = gen.getCurrentValue();
                if (currentValue instanceof Map) {
                    gen.writeString("");
                    return;
                }

                if( fieldName == null ) {
                    gen.writeNull();
                    return;
                }

                //反射获取字段类型  因此这里仅能获取到  最外层对象的字段信息，此外还出现字段名  被修改问题，与属性字段名不对应，因此多数情况下意义不大，而且覆盖了注解的使用。
                Field field = ReflectionUtils.findField(gen.getCurrentValue().getClass(), fieldName);
                if (field == null) {
                    gen.writeNull();
                    return;
                }
                if (Objects.equals(field.getType(), String.class)) {
                    //字符串型空值""
                    gen.writeString("");
                    return;
                } else if (Objects.equals(field.getType(), List.class)) {
                    //列表型空值返回[]
                    gen.writeStartArray();
                    gen.writeEndArray();
                    return;
                } else if (Objects.equals(field.getType(), Map.class)) {
                    //map型空值返回{}
                    gen.writeStartObject();
                    gen.writeEndObject();
                    return;
                } else {
                    gen.writeNull();
                }
            }
        });
        return objectMapper;
    }

}
