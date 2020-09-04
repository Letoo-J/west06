package com.mine.west;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mine.west.models.Blog;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class JacksonTest {

    @Test
    void testJackson() throws JsonProcessingException {
        //jackson的ObjectMapper转换对象:
        //实现json与java对象的转换
        ObjectMapper mapper = new ObjectMapper();

        Blog b = Blog.builder()
                .blogID(8)
                .accountID(2)
                .releaseTime(new Date())
                .likeNumber(23)
                .repostNumber(110)
                .commentNumber(15)
                .content("test-Jackson...")
                .collectNumber(15)
                .build();

        //java对象转换为json
        String jsonString = mapper.writeValueAsString(b);
        System.out.println(jsonString);

        //json转换为java对象(要有全参、无参构造函数)
        String json = "{\"blogID\":8,\"accountID\":2,\"releaseTime\":\"2020-07-24 21:05:54\",\n" +
                "\"likeNumber\":23,\"repostNumber\":110,\"commentNumber\":15,\"content\":\"test-Jackson...\"}";
        Blog bb = mapper.readValue(json,Blog.class);
        System.out.println("json->Blog:"+bb);
    }
}
