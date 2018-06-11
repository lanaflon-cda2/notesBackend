package com.douwe.notes.web.security;

import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
@EnableWebSecurity
@Named
public class SecurityConfigurator extends WebSecurityConfigurerAdapter {

//    @Autowired
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.
//            inMemoryAuthentication()
//                .passwordEncoder(new BCryptPasswordEncoder())
//                .withUser("ADMIN")
//                    .password("$2a$10$P61qkEmH6PhgQCBsDImOFeueoACJjfY2dXah9Y.La8N7/Jn5.SzOS")
//                    .roles("ADMIN", "CLIENT")
//                    .and()
//                .withUser("USER")
//                    .password("user")
//                    .roles("CLIENT");          
//    }
    
 
    
    
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth, UserDetailsService userService) throws Exception {
        super.configure(auth);
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/**/*.js", "/css/*.css", "/img/**", "/fonts/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .and()
                .httpBasic();
    }
}
