package com.mine.west.models;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RegisterAccount implements Serializable {
    private String username;
    private String password;
    private String mail;
    private String verifyInput;

    private static final long serialVersionUID = 1L;
}
