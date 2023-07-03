package com.wwan13.springjwtexample.controller;

import com.wwan13.springjwtexample.dto.LoginDto;
import com.wwan13.springjwtexample.dto.SignupDto;
import com.wwan13.springjwtexample.dto.TokenDto;
import com.wwan13.springjwtexample.jwt.JwtFilter;
import com.wwan13.springjwtexample.jwt.TokenProvider;
import com.wwan13.springjwtexample.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
public class AuthController {

    private final MemberService memberService;

    @PostMapping(value = "/login")
    public ResponseEntity login(@Valid @RequestBody LoginDto loginDto) {

        String jwt = memberService.login(loginDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return ResponseEntity.ok().headers(httpHeaders).body(new TokenDto(jwt));
    }

    @PostMapping(value = "/signup")
    public ResponseEntity signup(@Valid @RequestBody SignupDto signupDto) {
        return ResponseEntity.ok().body(memberService.signup(signupDto));
    }

    @GetMapping(value = "/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // user, admin 권한을 가진 토큰만 호츨 가능
    public ResponseEntity getMyUserInfo() {
        return ResponseEntity.ok().body(memberService.getCurrentUserWithAuthorities());
    }

    @GetMapping(value = "/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')") // admin 권한을 가진 토큰만 호츨 가능
    public ResponseEntity getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok().body(memberService.getUserWithAuthorities(username));
    }
}
