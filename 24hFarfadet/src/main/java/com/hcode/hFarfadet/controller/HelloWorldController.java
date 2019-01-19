package com.hcode.hFarfadet.controller;

import com.hcode.hFarfadet.Model.Lampe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
public class HelloWorldController {

    @GetMapping("/testihm")
    public String testIhm(@RequestParam(name = "nameGET", required = false, defaultValue = "World")
                                   String nameGET,Model model) {
        model.addAttribute("nomTemplate", nameGET);
        return "testihm";
    }

    @PostMapping("/addlampe")
    public String testIhmSubmit(@ModelAttribute Lampe lampe) {
        lampe.setNom("/nom");
        System.out.println(lampe.getNom());
        return "testihm";
    }

}
