package com.planmate.server.config;

import com.planmate.server.service.member.MemberService;
import com.planmate.server.util.JwtCustomFilter;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Generated
/**
 * @author 지승언
 * security config file
 * */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//@PreAuthorize 어노테이션을 메소드 단위로 추가하기 위해
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        /**
         * 별도의 권한 없이 실행되어야 하는 api들의 end point를 설정하는 메서드이다.
         * */
        return new WebSecurityCustomizer() {
            @Generated
            @Override
            public void customize(WebSecurity web) {
                web.ignoring().antMatchers(
                        "/",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/health/**",
                        "/login/**",
                        "/token/**",
                        "/info/auth"
                );
            }
        };
    }

    /**
     * 각 End Point를 어떤 권한을 가진 사용자가 사용하게 만들지에 대한 메소드이다.
     * */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, MemberService memberService) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .cors()
                .configurationSource(corsConfigurationSource)
                .and()
                .csrf().disable()
                .cors().and()

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterAfter(new JwtCustomFilter(memberService), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/member/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/post/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/comment/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/tendinous/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/subject/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/auth/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/schedule/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/logout/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .antMatchers("/statistic/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .antMatchers("/planner/**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                .anyRequest().denyAll()
                .and().build();
    }
}