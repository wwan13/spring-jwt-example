package com.wwan13.springjwtexample.controller;

import com.wwan13.springjwtexample.entity.Member;
import com.wwan13.springjwtexample.util.CurrentUser;
import com.wwan13.springjwtexample.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/sample")
    public ResponseEntity<String> sampleApi(@CurrentUser User user) {

        System.out.println(user.getUsername());
        return ResponseEntity.ok().body("sample");
    }

    @GetMapping("/adminSample")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> adminSample() {
        return ResponseEntity.ok().body("sample");
    }

}
