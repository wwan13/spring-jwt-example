package com.wwan13.springjwtexample.dto;

import lombok.*;

import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginDto {

    @NonNull
    @Size(min = 3, max = 50)
    private String username;

    @NonNull
    @Size(min = 3, max = 100)
    private String password;

}
