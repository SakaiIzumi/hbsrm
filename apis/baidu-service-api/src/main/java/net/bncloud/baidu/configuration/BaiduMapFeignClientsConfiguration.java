package net.bncloud.baidu.configuration;

import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/06/09
 **/
public class BaiduMapFeignClientsConfiguration {


    /**
     * 添加类型解析
     * @return
     */
    @Bean
    public Decoder feignDecoder() {
        BaiduMappingJackson2HttpMessageConverter converter = new BaiduMappingJackson2HttpMessageConverter();
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters( converter);
        return new SpringDecoder(objectFactory);
    }

    public static class BaiduMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
        public BaiduMappingJackson2HttpMessageConverter() {

            List<MediaType> mediaTypes = new ArrayList<>();
            mediaTypes.add( MediaType.TEXT_PLAIN);
            mediaTypes.add( new MediaType("text","javascript"));
            this.setSupportedMediaTypes( mediaTypes );
        }
    }

}
