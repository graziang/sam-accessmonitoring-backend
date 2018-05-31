package g.graziano.sampepsserver.controller;


import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.web.bind.annotation.RestController
public class RestController {


    @GetMapping("test")
    public String test(){

        return "test";
    }


    @GetMapping("notest")
    public String notest(){

        return "notest";
    }
}
