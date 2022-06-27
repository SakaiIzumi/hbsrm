package net.bncloud.common.web.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;

import java.io.IOException;

public class LongToStringSerializer extends NumberSerializers.Base<Object> {
    private static final long serialVersionUID = 3611542171681620712L;

    public final static LongToStringSerializer instance = new LongToStringSerializer(Object.class);
    protected LongToStringSerializer(Class<?> cls) {
        super(cls, JsonParser.NumberType.LONG, "number");
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.toString());
    }
}
