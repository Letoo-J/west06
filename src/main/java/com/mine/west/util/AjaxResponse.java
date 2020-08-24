package com.mine.west.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
//@AllArgsConstructor  //全参构造函数
//@NoArgsConstructor   //无参构造函数 public AjaxResponse(){}
@ApiModel(value = "Ajax通用响应数据结构类")
public class AjaxResponse {

    @ApiModelProperty(value="请求是否处理成功")
    private boolean isok;   //请求是否成功

    @ApiModelProperty(value="请求响应状态码",example="200、400、500")
    private int code;       //状态码：200-成功 400-用户错误 500-后台错误

    @ApiModelProperty(value="请求结果描述信息")
    private String msg;     //提示信息

    @ApiModelProperty(value="请求结果数据")
    private Object data;    //返回数据

    public AjaxResponse(){}
    public AjaxResponse(Boolean isok, int code, String msg){
        this.isok = isok;
        this.code = code;
        this.msg = msg;
    }
    public AjaxResponse(Boolean isok, int code, String msg, Object data){
        this.isok = isok;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static AjaxResponse success(){
        AjaxResponse ajaxResponse = new AjaxResponse();
        ajaxResponse.setIsok(true);
        ajaxResponse.setCode(200);
        ajaxResponse.setMsg("请求响应成功！");
        return ajaxResponse;
    }

    public static AjaxResponse success(String msg){
        AjaxResponse ajaxResponse = new AjaxResponse();
        ajaxResponse.setIsok(true);
        ajaxResponse.setCode(200);
        ajaxResponse.setMsg(msg);
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

    public static AjaxResponse fail(int code, String msg){
        AjaxResponse ajaxResponse = new AjaxResponse();
        ajaxResponse.setIsok(false);
        ajaxResponse.setCode(code);
        ajaxResponse.setMsg(msg);
        return ajaxResponse;
    }

    public static AjaxResponse fail(int code, String msg,Object data){
        AjaxResponse ajaxResponse = new AjaxResponse();
        ajaxResponse.setIsok(false);
        ajaxResponse.setCode(code);
        ajaxResponse.setMsg(msg);
        ajaxResponse.setData(data);
        return ajaxResponse;
    }

}
