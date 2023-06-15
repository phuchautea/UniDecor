package uni.decor.config;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import uni.decor.entity.CustomUserDetail;

public class SecurityUtils {
    public static String getEmailPrincipal() {
        Authentication authentication = (SecurityContextHolder.getContext()).getAuthentication();
        String email = "";
        if (authentication instanceof AnonymousAuthenticationToken) {
            // Chưa đăng nhập
            email = "";
        } else if (authentication.getPrincipal() instanceof CustomUserDetail) {
            // Xác thực thông qua tài khoản thường
            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
            email = customUserDetail.getEmail();
        } else if (authentication.getPrincipal() instanceof DefaultOidcUser) {
            // Xác thực thông qua Gmail
            DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
            email = oidcUser.getClaim("email");
        }
        return email;
    }
}
