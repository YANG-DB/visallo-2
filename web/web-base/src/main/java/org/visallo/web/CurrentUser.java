package org.visallo.web;

import org.slf4j.MDC;
import org.visallo.core.user.User;
import org.visallo.web.util.RemoteAddressUtil;

import javax.servlet.http.HttpServletRequest;

import static org.visallo.core.logging.LogUtils.*;

public class CurrentUser {

    public static void set(HttpServletRequest request, User user) {
        request.setAttribute(CURRENT_USER_REQ_ATTR_NAME, user);
        setUserInLogMappedDiagnosticContexts(request);
    }

    public static User get(HttpServletRequest request) {
        return (User) request.getAttribute(CURRENT_USER_REQ_ATTR_NAME);
    }

    public static void clearUserFromLogMappedDiagnosticContexts() {
        MDC.remove(MDC_USER_ID);
        MDC.remove(MDC_USER_NAME);
        MDC.remove(MDC_CLIENT_IP_ADDRESS);
    }

    public static void setUserInLogMappedDiagnosticContexts(HttpServletRequest request) {
        User currentUser = CurrentUser.get(request);

        if (currentUser != null) {
            String userId = currentUser.getUserId();
            if (userId != null) {
                MDC.put(MDC_USER_ID, userId);
            }
            String userName = currentUser.getUsername();
            if (userName != null) {
                MDC.put(MDC_USER_NAME, userName);
            }
        }

        MDC.put(MDC_CLIENT_IP_ADDRESS, RemoteAddressUtil.getClientIpAddr(request));
    }

}
