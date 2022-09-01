package com.skillsdb.jobseekerservice;

import com.skillsdb.jobseekerservice.jwtfilter.JwtFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
public class JobseekerserviceApplication {

//	@Bean
//	public FilterRegistrationBean<JwtFilter> jwtFilter() {
//		FilterRegistrationBean fbean = new FilterRegistrationBean();
//		fbean.setFilter(new JwtFilter());
//		fbean.addUrlPatterns("/jobSeeker/*");
//		return fbean;
//	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {

		SpringApplication.run(JobseekerserviceApplication.class, args);
	}

}
