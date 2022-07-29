package com.example.springfirewallbug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpStatusRequestRejectedHandler;
import org.springframework.security.web.firewall.RequestRejectedHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@SpringBootApplication
@RestController
@EnableWebSecurity
public class SpringFirewallBugApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringFirewallBugApplication.class, args);
    }

    @RequestMapping("/foo")
    public String foo(WebRequest webRequest) {
        return "Hello " + webRequest.getHeader("X-Header");
    }

    @RequestMapping("/bar")
    public String bar() {
        return "Hello";
    }

    @Bean
    public RequestRejectedHandler requestRejectedHandler() {
        return new HttpStatusRequestRejectedHandler();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests((auth) -> auth.anyRequest().permitAll())
            .build();
    }

}
