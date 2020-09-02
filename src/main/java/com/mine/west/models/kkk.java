package com.mine.west.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor  //全参构造函数
@NoArgsConstructor   //无参构造函数 public AjaxResponse(){}
@Getter
@Setter
public class kkk {
    String username;
    String password;
    Boolean rememberMe;
}
