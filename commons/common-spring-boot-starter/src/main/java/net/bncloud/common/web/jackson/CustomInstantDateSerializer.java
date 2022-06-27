package net.bncloud.common.web.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.bncloud.common.util.DateUtil;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @ClassName CustomInstantDateSerializer
 * @Author Administrator
 * @Date 2021/5/12
 * @Version V1.0
 **/
public class CustomInstantDateSerializer  extends StdSerializer<Instant> {

    private static final long serialVersionUID = 1L;

    private static DateTimeFormatter format = DateTimeFormatter.ofPattern(DateUtil.PATTERN_DATETIME);

    public CustomInstantDateSerializer() {
        this(null);
    }

    public CustomInstantDateSerializer(Class<Instant> t) {
        super(t);
    }

    @Override
    public void serialize(Instant instant, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (instant == null) {
            return;
        }
        String jsonValue = format.format(instant.atZone(ZoneId.systemDefault()));
        jsonGenerator.writeString(jsonValue);
    }
}
