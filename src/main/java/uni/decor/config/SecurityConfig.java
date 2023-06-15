package uni.decor.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
//import uni.decor.service.CustomOAuth2UserService;
import uni.decor.entity.User;
import uni.decor.repository.IUserRepository;
import uni.decor.service.CustomUserDetailService;
import uni.decor.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired
    private UserService userService;
    @Autowired
    private IUserRepository userRepository;
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailService();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService());
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
    protected void securityFilterChain(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        final OidcUserService delegate = new OidcUserService();

        return (userRequest) -> {
            // Delegate to the default implementation for loading a user
            OidcUser oidcUser = delegate.loadUser(userRequest);

            // TODO
            // 1) Fetch the authority information from the protected resource using accessToken
            OAuth2AccessToken accessToken = userRequest.getAccessToken();

            // Save User
            userService.saveOauthUser(oidcUser.getEmail(), oidcUser.getAttribute("name"));
            User user = userService.getUserByEmail(oidcUser.getEmail());

            // 2) Map the authority information to one or more GrantedAuthority's and add it to mappedAuthorities
            String[] roles = userRepository.getRolesOfUser(user.getId());
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            for(String role : roles)
            {
                mappedAuthorities.add(new SimpleGrantedAuthority(role));
            }

            // 3) Create a copy of oidcUser but use the mappedAuthorities instead
            oidcUser = new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
            return oidcUser;
        };
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
            Exception {
        return http.csrf().disable().authorizeHttpRequests(auth -> auth
                                .requestMatchers("/user/login", "/user/register", "/oauth2/authorization/google")
                                .anonymous()
                                .requestMatchers( "/template/**", "/**", "/user/logout", "/error", "/cart/**", "/product/**", "/order/**", "/pay/**")
                                .permitAll()
                                .requestMatchers("/user/account")
                                .hasAuthority("user")
                                .anyRequest().authenticated()
                )
                .logout(logout -> logout.logoutUrl("/user/logout")
                        .logoutSuccessUrl("/user/login")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)

                        .permitAll()

                )
                .formLogin(formLogin -> formLogin.loginPage("/user/login")
                        .loginProcessingUrl("/user/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/user/account")
                        .permitAll()
                )
                .oauth2Login(
                        oauth2Login -> oauth2Login
                                .loginPage("/user/login")
                                .failureUrl("/user/login?error")
                                .userInfoEndpoint(userInfoEndpoint ->
                                                userInfoEndpoint
//                                                .userService(customOAuth2UserService)
                                                        .oidcUserService(this.oidcUserService())
                                )
//                                .successHandler(
//                                        (request, response,
//                                         authentication) -> {
//                                            DefaultOidcUser oidcUser =
//                                                    (DefaultOidcUser) authentication.getPrincipal();
//                                            userService.saveOauthUser(oidcUser.getEmail(), oidcUser.getAttribute("name"));
//                                            response.sendRedirect("/user/account");
//                                        }
//                                )
                                .permitAll()
                )

                .rememberMe(rememberMe -> rememberMe.key("uniqueAndSecret")
                        .tokenValiditySeconds(86400)
                        .userDetailsService(userDetailsService())
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.accessDeniedPage("/403"))
                .build();
    }
}
