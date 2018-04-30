package com.douwe.notes.web.security;

import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
@EnableWebSecurity
@Named
public class SecurityConfigurator extends WebSecurityConfigurerAdapter {

    @Autowired
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.
            inMemoryAuthentication()
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser("ADMIN")
                    .password("admin")
                    .roles("ADMIN", "CLIENT")
                    .and()
                .withUser("USER")
                    .password("user")
                    .roles("CLIENT");
                
    }
    
    
//    @Inject
//    private UserDetailsService userService;
//    
//    
//    @Autowired
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
//        auth.userDetailsService(userService);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**/*.js", "/css/*.css", "/img/**", "/fonts/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/")
                .permitAll()
                .and()
                .httpBasic();
    }

//    public UserDetailsService getUserService() {
//        return userService;
//    }
//
//    public void setUserService(UserDetailsService userService) {
//        this.userService = userService;
//    }
}
