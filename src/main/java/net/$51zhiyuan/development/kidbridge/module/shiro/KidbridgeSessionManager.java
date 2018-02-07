package net.$51zhiyuan.development.kidbridge.module.shiro;

import net.$51zhiyuan.development.kidbridge.module.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.*;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.PatternMatcher;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

/**
 * shiro+redis整合实现
 */
public class KidbridgeSessionManager extends AbstractValidatingSessionManager  {

    private final Logger logger = LogManager.getLogger(KidbridgeSessionManager.class);

    private SessionFactory sessionFactory;

    private SessionDAO sessionDAO;

    public KidbridgeSessionManager(){
        this.sessionFactory = new SimpleSessionFactory();
    }

    @Override
    protected void onInvalidation(Session s, InvalidSessionException ise, SessionKey key) {
        super.onInvalidation(s, ise, key);
        this.onInvalidation(key);
    }

    @Override
    protected void onExpiration(Session s, ExpiredSessionException ese, SessionKey key) {
        super.onExpiration(s, ese, key);
        this.onInvalidation(key);
    }

    @Override
    protected void onExpiration(Session session) {
        if (session instanceof SimpleSession) {
            ((SimpleSession) session).setExpired(true);
        }
        this.onChange(session);
    }

    @Override
    protected void afterExpired(Session session) {
        this.delete(session);
    }

    @Override
    protected Session retrieveSession(SessionKey key) {
        Serializable sessionId = null;
        try {
            sessionId = this.getSessionId(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Session session = this.retrieveSession(sessionId);
        return session;
    }

    @Override
    protected Session doCreateSession(SessionContext initData) throws AuthorizationException {
        Session session = ((initData != null && initData.getHost() != null) ? new KidbridgeSession(initData.getHost()) : new KidbridgeSession());
        create(session,initData);
        return session;
    }

    @Override
    protected Collection<Session> getActiveSessions() {
        Collection<Session> active = this.sessionDAO.getActiveSessions();
        return active != null ? active : Collections.emptySet();
    }

    private void onInvalidation(SessionKey key) {

    }

    protected void delete(Session session) {
        this.sessionDAO.delete(session);
    }

    protected void onChange(Session session) {
        if(this.sessionDAO.readSession(session.getId()) != null){
            this.sessionDAO.update(session);
        }
    }

    protected void create(Session session,SessionContext initData) {
        if(WebUtils.isWeb(initData)){
            HttpServletRequest request = (HttpServletRequest) WebUtils.getRequest(initData);
            if(this.isLoginRequest(request)){
                this.sessionDAO.create(session);
            }
        }
    }

    private boolean isLoginRequest(ServletRequest servletRequest){
        PatternMatcher pathMatcher = new AntPathMatcher();
        String requestURI = WebUtils.getPathWithinApplication(WebUtils.toHttp(servletRequest));
        for(String path : Configuration.SYSTEM_SHIRO_LOGIN_URL){
            if(pathMatcher.matches(path,requestURI)){
                return true;
            }
        }
        return false;
    }

    protected Serializable getSessionId(SessionKey sessionKey) {
        String token = null;
        if(WebUtils.isWeb(sessionKey)){
            HttpServletRequest request = (HttpServletRequest) WebUtils.getRequest(sessionKey);
            String requestToken = request.getHeader(Configuration.SYSTEM_REQUEST_TOKEN);
            if(!StringUtils.isBlank(requestToken) && !isLoginRequest(request)){
                token = requestToken;
            }
        }
        return sessionKey.getSessionId() == null ? token : sessionKey.getSessionId();
    }

    protected Session newSessionInstance(SessionContext context) {
        return this.getSessionFactory().createSession(context);
    }

    protected Session retrieveSession(Serializable sessionId) throws UnknownSessionException {
        return sessionId == null ? null : this.sessionDAO.readSession(sessionId);
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionDAO getSessionDAO() {
        return this.sessionDAO;
    }

    public void setSessionDAO(SessionDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }
}
