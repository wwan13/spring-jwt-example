package com.wwan13.springjwtexample.service;

import com.wwan13.springjwtexample.dto.LoginDto;
import com.wwan13.springjwtexample.dto.SignupDto;
import com.wwan13.springjwtexample.entity.Authority;
import com.wwan13.springjwtexample.entity.Member;
import com.wwan13.springjwtexample.jwt.TokenProvider;
import com.wwan13.springjwtexample.repository.MemberRepository;
import com.wwan13.springjwtexample.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public String login(LoginDto loginDto) {

        if (!memberRepository.existsByUsername(loginDto.getUsername())) {
            throw new RuntimeException("가입되지 않은 유저입니다.");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenProvider.createToken(authentication);
    }

    public Member signup(SignupDto signupDto) {

        if (memberRepository.existsByUsername(signupDto.getUsername())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Member member = Member.builder()
                .username(signupDto.getUsername())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .nickname(signupDto.getNickname())
                .authorities(Collections.singleton(Authority.ROLE_USER))
                .activate(true)
                .build();

        return memberRepository.save(member);
    }

    public Member getUserWithAuthorities(String username) {
        return memberRepository.findOneWithAuthoritiesByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
    }

    public Member getCurrentUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(memberRepository::findOneWithAuthoritiesByUsername)
                .orElseThrow(() -> new IllegalArgumentException("현재 사용자가 없습니다."));
    }

}
