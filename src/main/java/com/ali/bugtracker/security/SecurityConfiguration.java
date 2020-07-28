package com.ali.bugtracker.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    //authentication
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.jdbcAuthentication()
                .usersByUsernameQuery("select email, password, 'true' "+
                        "from employee where email = ?")
                .authoritiesByUsernameQuery("select email, role "+
                        "from employee where email = ?")
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);

    }

    //authorization

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/register").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/dashboard").authenticated()
                .antMatchers("/profile").authenticated()
                .antMatchers("/home").authenticated()
                .antMatchers("/board/admin/**").hasRole("A")
                .antMatchers("/board/manager/**").hasRole("M")
                .antMatchers("/home").hasRole("M")
                .antMatchers("/board/programmer/**").hasRole("P")
                .antMatchers("/board/tester/**").hasRole("T")

                .and()
                .formLogin().loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/profile",false)
                .permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/?logout")
                .and()
                .rememberMe().rememberMeParameter("rememberMe").key("rememberMeKey");


    }

}
