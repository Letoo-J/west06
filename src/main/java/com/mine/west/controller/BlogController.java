package com.mine.west.controller;

import com.mine.west.models.Blog;
import com.mine.west.service.BlogService;
import com.mine.west.util.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping(value = "/user")
public class BlogController {

//    @Resource  //注入BlogService
//    BlogService _blogServive;

    /**
     * 根据ID，查询一篇博客
     * @param blogID
     * @return
     */
    //@GetMapping("/blogs/{id}")  作用相同
    @RequestMapping(value = "/blogs/{id}",method = RequestMethod.GET)
    public AjaxResponse getBlog(@PathVariable("id")Integer blogID){
        if(blogID == null){
            //TODO 抛出一个自定义异常
        }
        //TODO 数据库操作：
        //Blog b = new Blog(1);
        Blog b = Blog.builder()
                .blogID(blogID)
                .accountID(2)
                .releaseTime(new Date())
                .likeNumber(23)
                .repostNumber(110)
                .commentNumber(15)
                .content("神马都没有")
                .build();

        //打印日志
        log.info("get(查询)-blog："+b);

        AjaxResponse ajaxResponse = AjaxResponse.success(b,"查询成功");
        return ajaxResponse;
    }

    /**
     * 发布一篇博客
     * @param blog
     * @return
     */
    //@PostMapping("/blogs")
    @RequestMapping(value = "/blogs",method = RequestMethod.POST)
    public AjaxResponse addBlog(@RequestBody Blog blog){
        //数据库操作：
        //int num = _blogServive.addBlog(blog);

        //打印日志
        log.debug("post(新增)-blog："+blog);

        AjaxResponse ajaxResponse = AjaxResponse.success();
        return ajaxResponse;
    }

    /**
     * 根据ID，修改博客
     * @param blog
     * @return
     */
    //@PutMapping("/blogs")
    @RequestMapping(value = "/blogs",method = RequestMethod.PUT)
    public AjaxResponse updateBlog(@RequestBody Blog blog){
        if(blog.getBlogID() == null){
            //TODO 抛出一个自定义异常
        }
        //TODO 数据库操作：

        //打印日志
        log.warn("put(修改)-blog："+blog);

        AjaxResponse ajaxResponse = AjaxResponse.success();
        return ajaxResponse;
    }

    /**
     * 根据ID，删除一篇博客
     * @param blogID
     * @return
     */
    //@DeleteMapping("/blogs/{id}")
    @RequestMapping(value = "/blogs/{id}",method = RequestMethod.DELETE)
    public AjaxResponse deleteBlog(@PathVariable("id")Integer blogID){
        if(blogID == null){
            //TODO 抛出一个自定义异常
        }
        //TODO 数据库操作：

        //打印日志
        log.error("delete(删除)-blog-ID："+blogID);

        AjaxResponse ajaxResponse = AjaxResponse.success();
        return ajaxResponse;
    }
}
