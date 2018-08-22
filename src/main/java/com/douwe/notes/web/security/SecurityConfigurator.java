package com.douwe.notes.web.security;

import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

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
    private AccessDeniedHandler accessDeniedHhandler;


    @Autowired
    protected void configure(AuthenticationManagerBuilder auth, UserDetailsService userService) throws Exception {
        super.configure(auth);
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .authorizeRequests()
                    .antMatchers("/**/*.js", "/css/*.css", "/img/**", "/fonts/**")
                        .permitAll()
                    .antMatchers(HttpMethod.POST, "/api/cycles", "/api/niveaux", "/api/departements", "/api/semestres", "/api/annees")
                        .access("hasRole('ADMINISTRATEUR')")
                    .antMatchers(HttpMethod.PUT, "/api/cycles/**", "/api/niveaux/**", "/api/departements/**", "/api/semestres/**", "/api/annees/**")
                        .access("hasRole('ADMINISTRATEUR')")
                    .antMatchers(HttpMethod.DELETE, "/api/utilisateurs/**", "/api/cycles/**", "/api/niveaux/**", "/api/departements/**", "/api/semestres/**", "/api/annees/**")
                        .access("hasRole('ADMINISTRATEUR')")
                    .antMatchers(HttpMethod.GET, "/modules/utilisateur/views/liste.html", "/modules/cycle/**", "/modules/niveau/**", "/modules/departement/**", "/modules/semestre/**", "/modules/annee/**")
                        .access("hasRole('ADMINISTRATEUR')")
                    .antMatchers(HttpMethod.POST, "/api/etudiants", "/api/cours", "/api/credits", "/api/typeCours", "/api/uniteEns", "/api/programmes", "/api/enseignements", "/api/enseignants")
                        .access("hasAnyRole('CELLULE', 'DEPARTEMENT')")
                    .antMatchers(HttpMethod.GET, "/modules/rapport/**", "/modules/etudiant/**", "/modules/cours/**", "/modules/credit/**", "/modules/typecours/**", "/modules/uniteEnseignement/**", "/modules/programme/**", "/modules/enseignement/**", "/modules/enseignant/**")
                        .access("hasAnyRole('CELLULE', 'DEPARTEMENT')")
                    .anyRequest()
                        .authenticated()
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
                .exceptionHandling().accessDeniedPage("/error/403")
                .and()
                .httpBasic();
    }
}
