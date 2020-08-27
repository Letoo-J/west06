package com.mine.west.util;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.IOException;


/**
 * 	基于Jsoup的XSS过滤非法标签的工具类
 * 	过滤html中的xss字符
 * @author Lenovo
 *
 */
public class JsoupUtil {

    /**
     * 	使用自带的relaxed白名单
     * 	允许的便签有a,b,blockquote,br,caption,cite,
     code,col,colgroup,dd,div,dl,dt,
     em,h1,h2,h3,h4,h5,h6,i,img,li,
     ol,p,pre,q,small,span,strike,strong,
     sub,sup,table,tbody,td,tfoot,th,thead,tr,u,ul

     * 	以及a标签的href,img标签的src,align,alt,height,width,title等属性
     */
    private static final Whitelist whitelist = Whitelist.relaxed();
    //private static final Whitelist whitelist = Whitelist.basicWithImages();/basic();


    /** 配置过滤化参数,不对代码进行格式化 */
    private static final Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
    static {
        // 富文本编辑时一些样式是使用style来进行实现的
        // 比如红色字体 style="color:red;"。
        // 给所有标签添加style属性、title属性
        whitelist.addAttributes(":all", "style","title");
        //p标签的属性可用
        whitelist.addAttributes("p","class");
        //不可发链接
        //whitelist.removeAttributes("a", "href");
    }

    public static String clean(String content) {
        //System.out.println("过滤前1："+content);
        if(StringUtils.isNotBlank(content)){
            content = content.trim();
        }
        //System.out.println("过滤前2："+content);
        return Jsoup.clean(content, "", whitelist, outputSettings);
    }

    public static void main(String[] args) throws IOException {
        String text = "   <a href=\"http://www.baidu.com/a\" οnclick=\"alert(1);\">sss</a><script>alert(0);</script>sss   ";
        System.out.println(clean(text));
    }
}