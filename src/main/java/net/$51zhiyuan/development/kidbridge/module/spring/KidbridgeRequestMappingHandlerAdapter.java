package net.$51zhiyuan.development.kidbridge.module.spring;


import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

public class KidbridgeRequestMappingHandlerAdapter extends RequestMappingHandlerAdapter {

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        if(this.getCustomArgumentResolvers() != null){
            List<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers = new ArrayList<>(super.getArgumentResolvers());
            handlerMethodArgumentResolvers.addAll(0,this.getCustomArgumentResolvers());
            this.setArgumentResolvers(handlerMethodArgumentResolvers);
        }
    }

}
