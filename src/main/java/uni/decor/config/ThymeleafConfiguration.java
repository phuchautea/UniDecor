package uni.decor.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import uni.decor.util.NumberUtils;

import java.util.Enumeration;

@Configuration
public class ThymeleafConfiguration {
    @Bean
    public NumberUtils numberUtils() {
        return new NumberUtils();
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
