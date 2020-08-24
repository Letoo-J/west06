package com.mine.west.controller;

import com.mine.west.sliderVerification.VerificationCodeAdapterUtil;
import com.mine.west.util.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@ResponseBody
@RequestMapping(value = "/verification")
public class verificationController {

    //测试http://localhost:8080/scTest.html
    @GetMapping
    public AjaxResponse getVerification() {
        return AjaxResponse.success(VerificationCodeAdapterUtil.getRandomVerificationCodePlace());
    }
}
