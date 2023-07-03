package com.wwan13.springjwtexample.service;

import com.wwan13.springjwtexample.entity.Member;
import com.wwan13.springjwtexample.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findOneWithAuthoritiesByUsername(username)
                .map(member -> createUser(username, member))
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));
    }

    private User createUser(String username, Member member) {
        if(!member.isActivate()) {
            throw new RuntimeException("활성화되어 있지 않음");
        }

        List<GrantedAuthority> grantedAuthorityList = member.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.toString()))
                .collect(Collectors.toList());

        return new User(member.getUsername(), member.getPassword(), grantedAuthorityList);
    }
}
