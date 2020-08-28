package com.mine.west.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Maps;


/**将提示信息alert到前端
 * @author Lenovo
 *
 */
public class AjaxResponseWriter {

    /**alert到前端显示
     * @param resp
     * @param msg  返回的描述信息
     * @throws IOException
     */
    public static void println(HttpServletResponse resp , String msg )throws IOException{
        resp.setContentType("text/html; charset=UTF-8"); //转码
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = null;
        try {
            out = resp.getWriter();
            out.println("<script>alert('"+msg+"');</script>");
            out.flush();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        finally {
            if (out != null) {
                out.close();
            }
        }
        //out.println("history.back();");
    }

    /**
     * 写回数据到前端
     * @param request
     * @param response
     * @param message 返回的描述信息
     * @throws IOException
     */
    public static void write(HttpServletRequest request,HttpServletResponse response,String message) throws IOException{
        String contentType = "application/json";
        response.setContentType(contentType);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));

        Map<String, String> map = Maps.newLinkedHashMap();
        map.put("description", message);
        String result = JacksonHelper.toJson(map);
        PrintWriter out = response.getWriter();
        try{
            out.print(result);
            out.flush();
        } finally {
            out.close();
        }
    }
}

