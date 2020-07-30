package com.mine.west;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mine.west.models.Blog;
import com.mine.west.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.annotation.Resource;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

//有问题！！！！！！！！！！

@Slf4j
/** 当所测试代码有使用依赖注入时（@Resource / @Autowired）加上：
 *      @SpringBootTest  //构建spring的context环境
 *      @AutoConfigureMockMvc  //自动构建MockMvc对象
 *      @ExtendWith(SpringExtension.class)  //为当前测试加上springboot运行时的ioc容器环境（提供依赖注入）
 */
@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BlogControllerTest03 {

    //MockMvc对象：模拟发送http请求
    @Resource //= @Autowired
    private MockMvc mockMvc;

    @MockBean  //模拟此对象
    private BlogService blogService;

    /* //有@AutoConfigureMockMvc就不需要了：
    @BeforeAll  //在所有测试执行之前去执行！
    static  void setUp(){
        //对 BlogController类 进行测试
        mockMvc = MockMvcBuilders.standaloneSetup(new BlogController()).build();
    }*/

    @Test //测试方法
    public void addBlog() throws Exception{
        String jsonBlog = "{\"blogID\":8,\"accountID\":1,\"releaseTime\":\"2020-07-24 21:05:54\",\n" +
            "\"likeNumber\":23,\"repostNumber\":110,\"commentNumber\":15,\"content\":\"test-Jackson...\"}";

        ObjectMapper mapper = new ObjectMapper();
        Blog bb = mapper.readValue(jsonBlog,Blog.class);
        //打桩: 执行方法 blogService.addBlog(blog) 时，直接返回 1
        when(blogService.addBlog(bb)).thenReturn(1);  //error

        //执行http请求：
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                .request(HttpMethod.POST,"/user/blogs")
                .contentType("application/json")
                .content(jsonBlog)
        )//.andDo(print())  打印执行的内容.andReturn()  //返回执行结果
        .andDo(print())  //error
        .andReturn();


        //日志打印响应体：
        mvcResult.getResponse().setCharacterEncoding("utf-8");
        log.info(mvcResult.getResponse().getContentAsString());
    }

}
