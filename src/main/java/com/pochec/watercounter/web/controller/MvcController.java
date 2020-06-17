package com.pochec.watercounter.web.controller;

import com.pochec.watercounter.model.Water;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MvcController {

    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("addwater")
    public String addwater(Model model) {
        model.addAttribute("waterModel", new Water());
        return "addwater";
    }

    @GetMapping("deletewater")
    public String deleteWater(Model model) {
        model.addAttribute("waterModel", new Water());
        return "deletewater";
    }


    @GetMapping("/error")
    private String error() { return "error"; }
}
