package com.example.HealthcareConnect.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Properties;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/users/",
                        "/createUser",
                        "/forgetPasswordAction/*",
                        "/forgetPasswordAction",
                        "/resetPasswordAction",
                        "/api/users/sendEmail").permitAll()
                 .antMatchers( "/css/**").permitAll() //make css file public

                .antMatchers(HttpMethod.GET,
                        "/registration",
                        "/login",
                        "/forgetPassword",
                        "/resetPassword").permitAll()
                .anyRequest().authenticated()
                .and()

                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .permitAll();
//                .and()
//
//                .logout()
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .loginProcessingUrl("/perform_login")
//                .defaultSuccessUrl("/home.html", true)
//                .failureUrl("/login.html?error=true")
////                .failureHandler(authenticationFailureHandler())
//                .and()
//                .logout()
//                .logoutUrl("/perform_logout")
//                .deleteCookies("JSESSIONID");
////                .logoutSuccessHandler(logoutSuccessHandler());

    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailService)
                .passwordEncoder(bCryptPasswordEncoder());
        super.configure(auth);
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("medconnectofficial@gmail.com");
        mailSender.setPassword("wnwzosgxwwhpudlt");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }


}
