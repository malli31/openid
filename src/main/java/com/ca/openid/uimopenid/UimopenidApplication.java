package com.ca.openid.uimopenid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableAutoConfiguration
@Configuration
//@ComponentScan("com.ca.openid.uimopenid.MainController")
@SpringBootApplication
public class UimopenidApplication  {

	public static void main(String[] args) {
		SpringApplication.run(UimopenidApplication.class, args);
	}
	
}
