package com.mine.west.models;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginAccount implements Serializable {
    private String username;
    private String password;
    private String rememberMe;

    private static final long serialVersionUID = 1L;
}
