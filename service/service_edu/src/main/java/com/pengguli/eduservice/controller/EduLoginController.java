package com.pengguli.eduservice.controller;

import com.pengguli.commonutils.R;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eduservice/user")
//@CrossOrigin
public class EduLoginController {

    @PostMapping("login")
    public R loin(){
        return R.ok().data("token","peng");
    }

    @GetMapping("info")
    public R info(){
        return R.ok().data("roles","[admin]").data("name","admin").data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }
}
