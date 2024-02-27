package com.planmate.server.config;

import com.planmate.server.service.member.MemberService;
import com.planmate.server.service.token.TokenService;
import com.planmate.server.util.FilterChainExceptionHandler;
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
import org.springframework.web.cors.CorsConfigurationSource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//@PreAuthorize 어노테이션을 메소드 단위로 추가하기 위해
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsConfigurationSource corsConfigurationSource;
    private final MemberService memberService;
    private final TokenService tokenService;

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
                        "/token/**",
                        "/info/auth",
                        "/health/**",
                        "/login/**",
                        "/time/**"
                );
            }
        };
    }

    /**
     * 각 End Point를 어떤 권한을 가진 사용자가 사용하게 만들지에 대한 메소드이다.
     * */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .cors()
                .configurationSource(corsConfigurationSource)
                .and()
                .csrf().disable()
                .cors().and()

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterBefore(new FilterChainExceptionHandler(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtCustomFilter(memberService,tokenService), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/member/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/post/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/comment/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/tendinous/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/subject/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/auth/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/dday/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/logout/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .antMatchers("/statistic/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .antMatchers("/planner/**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                .antMatchers("/notice/**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                .and().build();
    }
}