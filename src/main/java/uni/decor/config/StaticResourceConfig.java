//package uni.decor.config;
//
//import org.springframework.beans.factory.annotation.Configurable;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class StaticResourceConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        String path= "file:/D:/temp/IMAGE_STORAGE/";
//        registry.addResourceHandler("/static/**")
//                .addResourceLocations(path);
//    }
//}
//
