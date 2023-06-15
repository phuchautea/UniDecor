package uni.decor.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uni.decor.common.CustomLogger;
import uni.decor.common.Enum;
import uni.decor.entity.User;
import uni.decor.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Enumeration;

@Controller
@RequestMapping("/user")
public class UserController {
//    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomLogger.info("authentication: "+authentication+"");
        if (authentication instanceof AnonymousAuthenticationToken) {
            CustomLogger.error("Chưa đăng nhập");
            return "user/login";
        } else {
            CustomLogger.warn("Đã đăng nhập");
            return "redirect:/";
        }
    }

    @GetMapping("/account")
    public String user(HttpServletRequest request) {
        HttpSession session = request.getSession();

        // Lấy tất cả thuộc tính có trong session
        Enumeration<String> attributeNames = session.getAttributeNames();

        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object attributeValue = session.getAttribute(attributeName);
            CustomLogger.error(attributeName);
            // Sử dụng attributeName và attributeValue tùy ý
        }
        return "user/account";
    }
    @GetMapping("/register")
    public String register(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("user", new User());
            CustomLogger.error("Chưa đăng nhập");
            return "user/register";
        } else {
            CustomLogger.error("Đã đăng nhập");
            return "redirect:/";
        }
    }
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model)
    {
//        if (bindingResult.hasErrors()) {
//            List<FieldError> errors = bindingResult.getFieldErrors();
//            for (FieldError error : errors) {
//                model.addAttribute(error.getField() + "_error",
//                        error.getDefaultMessage());
//            }
//            return "user/register";
//        }
        if(bindingResult.hasErrors()){
            model.addAttribute("user", user);
            return "user/register";
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setProvider(Enum.Provider.LOCAL);
        userService.save(user);
        return "redirect:/login";
    }
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/loginSuccess")
    public String getLoginInfo(Model model, OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());
        //...
        return "loginSuccess";
    }


}
