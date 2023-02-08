package cn.iatc.web.config;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * MVC配置
 */
@Configuration
@Data
public class WebMvcConfig implements WebMvcConfigurer, ObjectSerializer {

    private static final long MAX_AGE_SECS = 3600;

    private static final int FAST_JSON_CONVERTER_INDEX = 3;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
                .maxAge(MAX_AGE_SECS);
    }

    //返回的时间格式 设置fastjson
//    @Bean
    private FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");
        config.setSerializerFeatures(
                SerializerFeature.WriteMapNullValue,    //Map字段如果为null,输出为[],而非null
                SerializerFeature.DisableCircularReferenceDetect  //禁止循环引用, 解决出现"$ref": "$.data.list[0].category"，这是因为转换json时，同一个对象用过，之后使用该对象就报地址
        );
        // 解决springdoc中，字符串影响 swagger不显示问题, 是否会有其他问题，待验证，目前没有
//        config.getSerializeConfig().put(String.class, this);
        converter.setFastJsonConfig(config);
        return converter;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 解决springdoc中，字符串影响 swagger不显示问题, 是否会有其他问题，待验证，目前没有
        /**
         * FastJsonHttpMessageConverter 在 HttpMessageConverter 列表中的位置
         * 若 FastJsonHttpMessageConverter 在 StringHttpMessageConverter 前被调用，会导致对string进行二次转换
         * 由于 StringHttpMessageConverter 在第1和2的位置上，要排在其后，并且排在jacksonConverter之前，故位置为3
         */
        converters.add(FAST_JSON_CONVERTER_INDEX, fastJsonHttpMessageConverter());
    }

    // 配置编码方式，防止汉字乱码
    @Bean
    public CharacterEncodingFilter characterEncodingFilter(){
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

    // 解决springdoc中，字符串影响 swagger不显示问题
    // 参考：https://www.cnblogs.com/6543x1/p/15816139.html
    @Override
    public void write(JSONSerializer jsonSerializer, Object object, Object fieldName, Type fileType, int features) throws IOException {
        SerializeWriter out = jsonSerializer.getWriter();
        out.write(object.toString());
    }
}