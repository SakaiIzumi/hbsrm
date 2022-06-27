package net.bncloud.security.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.Map;

public class BncOAuth2ExceptionJackson2Serializer extends StdSerializer<OAuth2Exception> {
    private static final long serialVersionUID = 1724619995219829510L;

    protected BncOAuth2ExceptionJackson2Serializer() {
        super(OAuth2Exception.class);
    }

    @Override
    public void serialize(OAuth2Exception e, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("status", e.getHttpErrorCode());
        jsonGenerator.writeStringField("error", e.getOAuth2ErrorCode());
        String errorMessage = e.getMessage();
        if (errorMessage != null) {
            errorMessage = HtmlUtils.htmlEscape(errorMessage);
        }
        jsonGenerator.writeStringField("message", errorMessage);
        if (e.getAdditionalInformation() != null) {
            for (Map.Entry<String, String> entry : e.getAdditionalInformation().entrySet()) {
                String key = entry.getKey();
                String add = entry.getValue();
                jsonGenerator.writeStringField(key, add);
            }
        }
        jsonGenerator.writeEndObject();
    }
}
