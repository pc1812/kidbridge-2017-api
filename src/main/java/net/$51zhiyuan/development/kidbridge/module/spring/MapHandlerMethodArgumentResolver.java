package net.$51zhiyuan.development.kidbridge.module.spring;

import net.$51zhiyuan.development.kidbridge.module.json.KidbridgeObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * json请求字符串转MAP集合
 */
public class MapHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final KidbridgeObjectMapper kidbridgeObjectMapper = new KidbridgeObjectMapper();

    private final Logger logger = LogManager.getLogger(MapHandlerMethodArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        return parameter.hasParameterAnnotation(RequestBody.class) && parameter.getParameterType() == Map.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Map param = null;
        HttpServletRequest request = ((HttpServletRequest)webRequest.getNativeRequest());
        String contentType = request.getContentType();
        try{
            if(!StringUtils.isBlank(contentType)){
                if(MediaType.valueOf(contentType).isCompatibleWith(MediaType.APPLICATION_JSON)){
                    param = this.kidbridgeObjectMapper.readValue(StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8")), HashMap.class);
                }
            }
        }catch (Exception e){
            this.logger.error(e.getMessage(),e);
        }
        return param == null ? new HashMap() : param;
    }

}
