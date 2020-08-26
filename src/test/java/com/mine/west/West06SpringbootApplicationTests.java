package com.mine.west;

import com.mine.west.dao.AccountMapper;
import com.mine.west.dao.PersMapper;
import com.mine.west.models.Account;
import com.mine.west.models.Pers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class West06SpringbootApplicationTests {
    //日志记录器：Logger
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    private PersMapper persMapper;

    @Test
    void contextLoads() {
        //日志级别(可调整)：
        //由低到高：trace < debug < info < warn < error
        logger.trace("此为trace跟踪日志信息。。。");
        logger.debug("此为debug调试日志信息。。。");
        //springboot默认默认指定为info级别(root级别)，只输出>=info级别的日志
        logger.info("此为info日志");
        logger.warn("此为warn警告日志");
        logger.error("此为error日志");
    }

    @Test
    public void mapperTest1() {
        Account a = accountMapper.findRolesByName("Mickey");
        System.out.println("[account]: "+a);
    }

    @Test
    public void mapperTest2() {
        List<Pers> a = persMapper.findPermsByRoleId(1);
        System.out.println("【List<Pers>】: \n "+a);
    }

}
