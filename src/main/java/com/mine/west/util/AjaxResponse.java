package com.mine.west.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//处理ajax返回值
/**
 * @ Data 在编译期自动生成:
 *  1.成员变量的get和set方法
 *  2.equals方法
 *  3.canEqual方法
 *  4.hashCode方法
 *  5.toString方法
 */
@Data
@AllArgsConstructor  //全参构造函数
@NoArgsConstructor   //无参构造函数 public AjaxResponse(){}
public class AjaxResponse {

    //请求是否成功
    private boolean isok;
    //状态码
    private int code;   //200-成功 400-用户错误 500-后台错误
    //提示信息
    private String msg;
    //返回数据
    private Object data;

    public static AjaxResponse success(){
        AjaxResponse ajaxResponse = new AjaxResponse();
        ajaxResponse.setIsok(true);
        ajaxResponse.setCode(200);
        ajaxResponse.setMsg("请求响应成功！");
        return ajaxResponse;
    }

    public static AjaxResponse success(Object obj){
        AjaxResponse ajaxResponse = new AjaxResponse();
        ajaxResponse.setIsok(true);
        ajaxResponse.setCode(200);
        ajaxResponse.setMsg("请求响应成功！");
        ajaxResponse.setData(obj);
        return ajaxResponse;
    }

    public static AjaxResponse success(Object obj,String msg){
        AjaxResponse ajaxResponse = new AjaxResponse();
        ajaxResponse.setIsok(true);
        ajaxResponse.setCode(200);
        ajaxResponse.setMsg(msg);
        ajaxResponse.setData(obj);
        return ajaxResponse;
    }

}
