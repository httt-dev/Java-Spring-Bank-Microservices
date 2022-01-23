package com.ngx.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@RefreshScope
@EnableFeignClients
@ComponentScans({@ComponentScan("com.ngx.accounts.controller")})
@EnableJpaRepositories("com.ngx.accounts.repository")
@EntityScan("com.ngx.accounts.model")
public class AccountsApplication {

	public static void main(String[] args) {
		//https://springhow.com/spring-boot-change-context-path/
		//System.setProperty("server.servlet.context-path","/springhow");
		SpringApplication.run(AccountsApplication.class, args);
	}

}
