package kz.bitlab.group21boot.config;

import kz.bitlab.group21boot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception{
        builder.userDetailsService(userService);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/css/**", "/js/**").permitAll()
                .antMatchers("/").permitAll();

        http
                .formLogin()
                .loginProcessingUrl("/signin").permitAll()
                .loginPage("/enter").permitAll()
                .usernameParameter("email")
                .passwordParameter("pass")
                .defaultSuccessUrl("/profile")
                .failureUrl("/enter?error");

        http
                .logout().permitAll()
                .logoutSuccessUrl("/enter")
                .logoutUrl("/exit").permitAll();

        http.csrf().disable();

    }
}
