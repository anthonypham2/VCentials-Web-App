package com.example.application.security;

import com.example.application.views.login.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration
        extends VaadinWebSecurity  {

    //Gets the database
    @Autowired
    private DataSource dataSource;

    //Queries for a matching username and gets the assigned role
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username,password,enabled "
                        + "from userInfo "
                        + "where username = ?")
                .authoritiesByUsernameQuery("select username,authority "
                        + "from authorities "
                        + "where username = ?");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Delegating the responsibility of general configurations
        // of http security to the super class. It's configuring
        // the followings: Vaadin's CSRF protection by ignoring
        // framework's internal requests, default request cache,
        // ignoring public views annotated with @AnonymousAllowed,
        // restricting access to other views/endpoints, and enabling
        // NavigationAccessControl authorization.
        // You can add any possible extra configurations of your own
        // here (the following is just an example):
        // http.rememberMe().alwaysRemember(false);
        // Configure your static resources with public access before calling
        // super.configure(HttpSecurity) as it adds final anyRequest matcher

        http
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/console/**").permitAll();
                })
                .authorizeHttpRequests(auth -> auth.requestMatchers(new AntPathRequestMatcher("/public/**"))
                .permitAll()
        );

        super.configure(http);


        // This is important to register your login view to the
        // navigation access control mechanism:
        setLoginView(http, LoginView.class);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(httpSecurityHeadersConfigurer -> {
                    httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
                });
    }
    

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Customize your WebSecurity configuration.

        super.configure(web);

    }
}
