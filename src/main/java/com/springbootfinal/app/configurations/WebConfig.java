package com.springbootfinal.app.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// CSS 및 다른 정적 리소스를 위한 기본 핸들러 추가
	    registry.addResourceHandler("/css/**")
	        .addResourceLocations("classpath:/static/css/");
	    registry.addResourceHandler("/js/**")
	        .addResourceLocations("classpath:/static/js/");
	    registry.addResourceHandler("/images/**")
	        .addResourceLocations("classpath:/static/images/");
	    registry.addResourceHandler("/longWeather/**")
        .addResourceLocations("classpath:/static/longWeather/");
	}
	
	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
	
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/writeForm").setViewName("views/writeForm");
		registry.addViewController("/writeBoard").setViewName("views/writeForm");
		// 단기예보
		registry.addViewController("/weather").setViewName("weather/index");
		// 숙소
		registry.addViewController("/Residence").setViewName("views/Residence/Residence");
		// 통합 예보(단기+중기)
		registry.addViewController("/weather/form").setViewName("weather/weatherResult");
		// 기본페이지 폼 뷰 전용 컨트롤러 설정
		registry.addViewController("/").setViewName("main/main");
		// 로그인 폼 뷰 전용 컨트롤러 설정
		registry.addViewController("/loginForm").setViewName("member/loginForm");
		// 회원가입 폼
		registry.addViewController("/joinForm").setViewName("member/memberJoinForm");
		// 고객센터
		registry.addViewController("/helper").setViewName("views/helper");
	}
	
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")  // 특정 도메인을 허용하거나 '*'로 모든 도메인 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }

}
