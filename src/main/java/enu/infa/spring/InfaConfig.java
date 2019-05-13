package enu.infa.spring;


import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;


@Configuration
@EnableOAuth2Sso
@RestController
public class InfaConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
   //     http.antMatcher("/**").authorizeRequests().antMatchers("/" , "/login**", "/webjars/**", "/error**", "/css/**", "/js/**", "/images/**").permitAll().anyRequest().authenticated().and().logout().logoutSuccessUrl("/").permitAll().and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

    http.authorizeRequests().anyRequest().permitAll().and().logout().logoutSuccessUrl("/").permitAll().and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

    }


    /**
     * We want to filter what is public data about the person who is logged in.
     * Only the name is curently being shown instead of the standard principal object
     * @param principal
     * @return
     */
    @RequestMapping({ "/user"})
    public Map<String, String> user(Principal principal) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("name", principal.getName());
        return map;
    }
}
