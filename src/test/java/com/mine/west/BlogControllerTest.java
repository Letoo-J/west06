package com.mine.west;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@Slf4j
public class BlogControllerTest {

    //MockMvc对象：模拟发送http请求
    private static MockMvc mockMvc;

    @BeforeAll  //在所有测试执行之前去执行！
    static  void setUp(){
        //对 BlogController类 进行测试
//        mockMvc = MockMvcBuilders.standaloneSetup(new BlogController()).build();
    }

    @Test //测试方法
    public void addBlog() throws Exception{
        String jsonBlog = "{\"blogID\":8,\"accountID\":2,\"releaseTime\":\"2020-07-24 21:05:54\",\n" +
            "\"likeNumber\":23,\"repostNumber\":110,\"commentNumber\":15,\"content\":\"test-Jackson...\"}";

        //执行http请求：
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                .request(HttpMethod.POST,"/user/blogs")
                .contentType("application/json")
                .content(jsonBlog)
        )//.andDo(printf)  打印执行的内容.andReturn()  //返回执行结果
        .andExpect(MockMvcResultMatchers.status().isOk())
                //期望请求返回的json结果里面的 code = 200：
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
        .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("请求响应成功！"))
        .andReturn();


        //日志打印响应体：
        mvcResult.getResponse().setCharacterEncoding("utf-8");
        log.info(mvcResult.getResponse().getContentAsString());
    }

}
