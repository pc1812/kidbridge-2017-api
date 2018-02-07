package net.$51zhiyuan.development.kidbridge.module.shiro;

import net.$51zhiyuan.development.kidbridge.enums.Role;
import net.$51zhiyuan.development.kidbridge.exception.KidbridgeSystemException;
import net.$51zhiyuan.development.kidbridge.service.AuthcService;
import net.$51zhiyuan.development.kidbridge.service.TeacherService;
import net.$51zhiyuan.development.kidbridge.service.UserService;
import net.$51zhiyuan.development.kidbridge.ui.model.Teacher;
import net.$51zhiyuan.development.kidbridge.ui.model.User;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * shiro授权认证实现
 */
public class KidbridgeAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthcService authcService;

    @Autowired
    private TeacherService teacherService;

    /**
     * 用户权限授权
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        Teacher teacher = this.teacherService.get(new Teacher(){
            @Override
            public User getUser() {
                return new User(){
                    @Override
                    public Integer getId() {
                        return (int)principals.getPrimaryPrincipal();
                    }
                };
            }
        });
        simpleAuthorizationInfo.addRole(teacher == null ? Role.NORMAL.name(): Role.TEACHER.name());
        return simpleAuthorizationInfo;
    }

    /**
     * 用户登录认证
     * @param authenticationToken
     * @return
     * @throws KidbridgeSystemException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws KidbridgeSystemException {
        User user = null;
        if(authenticationToken instanceof KidbridgeSimpleLogin){
            // 如果当前用户登录方式为账号密码登录
            KidbridgeSimpleLogin token = (KidbridgeSimpleLogin) authenticationToken;
            // 验证用户名与密码是否匹配
            user  = this.userService.simpleLogin((String) token.getPrincipal(), token.getCredentials() == null ? null : new String((char[]) token.getCredentials()));
        }else if(authenticationToken instanceof KidbridgeAuthLogin){
            // 如果当前用户登录方式为授权登录
            KidbridgeAuthLogin token = (KidbridgeAuthLogin) authenticationToken;
            try {
                // 通过授权code获取openid，验证是否已与用户进行关联
                user  = this.authcService.authcLogin(token.getCredentials() == null ? null : new String((char[]) token.getCredentials()),token.getType());
            } catch (Exception e) {
                throw new KidbridgeSystemException(e.getMessage(),e);
            }
        }
        AuthenticationInfo info = new SimpleAuthenticationInfo(user.getId(), authenticationToken.getCredentials(), super.getName());
        return info;
    }

}
