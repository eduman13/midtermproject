package com.ironhack.midtermproject.security;

import com.ironhack.midtermproject.service.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled=true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Autowired
  EntryPoint entryPoint;
  
  @Bean
  public PasswordEncoder passwordEncoder () {
    return new BCryptPasswordEncoder();
  }
  
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
      .userDetailsService(userDetailsService)
      .passwordEncoder(passwordEncoder);
  }

  @Override
  public void configure(HttpSecurity httpSecurity) throws Exception {

    httpSecurity.httpBasic().authenticationEntryPoint(entryPoint);

    httpSecurity.logout().logoutUrl("/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID");

    httpSecurity.csrf().disable();
    httpSecurity.authorizeRequests().antMatchers("/").permitAll().and().authorizeRequests().antMatchers("/h2-console/**").permitAll();

    httpSecurity.headers().frameOptions().disable();

    httpSecurity.authorizeRequests()
      .mvcMatchers("/loggedin").authenticated()
      .mvcMatchers("/roles").hasAuthority("ROLE_TECHNICIAN")
      .mvcMatchers("/admin").hasAuthority("ROLE_ADMIN")
      .mvcMatchers(HttpMethod.POST, "/admin/create_saving/{id}").hasAuthority("ROLE_ADMIN")
      .mvcMatchers(HttpMethod.PATCH, "/account_holder/get_balance/{id}").hasAuthority("ROLE_ACCOUNT_HOLDER")
      .mvcMatchers(HttpMethod.PATCH, "/third_party/credit_account/{id_account}").hasAuthority("ROLE_THIRD_PARTY")
      .anyRequest().permitAll();
  }
}
