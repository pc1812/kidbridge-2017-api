package net.$51zhiyuan.development.kidbridge.module.shiro;

import net.$51zhiyuan.development.kidbridge.module.Util;
import net.$51zhiyuan.development.kidbridge.ui.model.Message;
import net.$51zhiyuan.development.kidbridge.ui.model.MessageType;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 角色验证实现
 */
public class KidbridgeRolesAuthorizationFilter extends RolesAuthorizationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Util.printJsonMessage(response,new Message(MessageType.UNROLE,"请求未授权"));
        return false;
    }
}
