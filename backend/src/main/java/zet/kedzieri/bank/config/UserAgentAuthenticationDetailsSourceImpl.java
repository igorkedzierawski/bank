package zet.kedzieri.bank.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

@Component
public class UserAgentAuthenticationDetailsSourceImpl extends WebAuthenticationDetailsSource {

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
        WebAuthenticationDetails details = super.buildDetails(context);
        String userAgent = context.getHeader("User-Agent");
        return new WebAuthenticationDetailsWithUserAgent(details.getRemoteAddress(),
                details.getSessionId(), userAgent);
    }

    @Getter
    public static class WebAuthenticationDetailsWithUserAgent extends WebAuthenticationDetails {

        private final UserAgentInfo userAgent;

        public WebAuthenticationDetailsWithUserAgent(String remoteAddress, String sessionId, String userAgent) {
            super(remoteAddress, sessionId);
            this.userAgent = parseUserAgent(userAgent);
        }

    }

    public static UserAgentInfo parseUserAgent(String userAgent) {
        UserAgentInfo result = new UserAgentInfo("Unknown", "Unknown");
        if (userAgent.matches(".*Firefox.*")) {
            result.setBrowser("Firefox");
        } else if (userAgent.matches(".*Chrome.*")) {
            result.setBrowser("Chrome");
        } else if (userAgent.matches(".*Safari.*")) {
            result.setBrowser("Safari");
        } else if (userAgent.matches(".*Edge.*")) {
            result.setBrowser("Microsoft Edge");
        } else if (userAgent.matches(".*Opera.*")) {
            result.setBrowser("Opera");
        } else if (userAgent.matches(".*IE|Trident.*")) {
            result.setBrowser("Internet Explorer");
        } else if (userAgent.matches(".*(?!Trident.*?rv:)(MSIE).*")) {
            result.setBrowser("Internet Explorer (Older Version)");
        }
        if (userAgent.matches(".*iPhone.*")) {
            result.setDevice("iPhone");
        } else if (userAgent.matches(".*iPad.*")) {
            result.setDevice("iPad");
        } else if (userAgent.matches(".*Android.*")) {
            result.setDevice("Android Device");
        } else if (userAgent.matches(".*Macintosh|Mac OS X.*")) {
            result.setDevice("Mac");
        } else if (userAgent.matches(".*Windows.*")) {
            result.setDevice("Windows PC");
        } else if (userAgent.matches(".*Linux.*")) {
            result.setDevice("Linux PC");
        } else if (userAgent.matches(".*(BlackBerry|BB10|PlayBook).*")) {
            result.setDevice("BlackBerry");
        } else if (userAgent.matches(".*Windows Phone.*")) {
            result.setDevice("Windows Phone");
        } else if (userAgent.matches(".*Xbox.*")) {
            result.setDevice("Xbox");
        }
        return result;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class UserAgentInfo {
        private String browser;
        private String device;
    }

}
