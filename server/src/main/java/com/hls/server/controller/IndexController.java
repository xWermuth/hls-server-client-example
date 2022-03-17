package com.hls.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller()
@RequestMapping("/")
public class IndexController {
    @GetMapping("*")
    public String greeting(@RequestParam(name = "adnumber", required = true) String adnumber,
            Model model) {
        model.addAttribute("adnumber", adnumber);
        return "ad";
    }

}
