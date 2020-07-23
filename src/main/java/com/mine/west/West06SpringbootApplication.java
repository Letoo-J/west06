package com.mine.west;

import ch.qos.logback.core.spi.FilterReply;
import com.mine.west.dao.RelayMapper;
import com.mine.west.models.Relay;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.SpringVersion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*启动程序
 *
 */
//@EnableCaching  //使用缓存
//@RestController : = @Controller + @ResponseBody
@MapperScan("com.mine.west.dao")
@SpringBootApplication
@Slf4j  //实现slf4j： log.info("233333");
public class West06SpringbootApplication {

    public static void main(String[] args) {

        SpringApplication.run(West06SpringbootApplication.class, args);
        //System.out.println("!!!!"+SpringVersion.getVersion());
        //log.info("233333");
    }

}
