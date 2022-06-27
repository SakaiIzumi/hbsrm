package net.bncloud.common.web.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 *
 * @ClassName BigDecimalSerializer
 * @Author Administrator
 * @Date 2021/5/13
 * @Version V1.0
 **/
public class AmountSerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (value != null) {
            BigDecimal bigDecimal = value.setScale(2, RoundingMode.HALF_UP);
            // 整数
            if( new BigDecimal( bigDecimal.intValue() ).compareTo( bigDecimal ) == 0  ){
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                jsonGenerator.writeString( decimalFormat.format(bigDecimal));
                return;
            }
            jsonGenerator.writeNumber( bigDecimal );
        } else {
            BigDecimal bigDecimal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            jsonGenerator.writeNumber(bigDecimal);
        }

    }

    public static void main(String[] args) {

//        DecimalFormat decimalFormat = new DecimalFormat("0.00");
//        String format = decimalFormat.format(bigDecimal);
//        System.out.println(format);

//        BigDecimal bigDecimal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
//        System.out.println( bigDecimal);


    }

}
