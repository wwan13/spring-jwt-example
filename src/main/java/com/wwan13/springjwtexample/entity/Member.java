package com.wwan13.springjwtexample.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;

    private String password;

    private String nickname;

    private boolean activate;

    @Enumerated(EnumType.STRING)
    private Set<Authority> authorities;

}
