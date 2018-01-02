package main.tlf.controller;

import main.tlf.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Calendar;

@Controller
@RequestMapping("/tlf")
public class tlfController {

    @RequestMapping("/test1")
    public String test(Model model){
        model.addAttribute("user",new User(true,"23"));
//        model.addAttribute("user",new User(false,"23"));
        return "tlf/index";
    }
    @RequestMapping("/test2")
    public String test2(Model model){
//        model.addAttribute("user",new User(true,"23"));
        model.addAttribute("user",new User(false,"23"));
        return "tlf/index";
    }
    @RequestMapping("/test3")
    public String test3(Model model){
//        model.addAttribute("user",new User(true,"23"));
        model.addAttribute("user",new User(false,null));
        return "tlf/index";
    }

    @RequestMapping("/test4")
    public String test4(Model model){
        model.addAttribute("today", Calendar.getInstance().getTime());
        model.addAttribute("user",new User(false,"  "));
        return "tlf/index";
    }
}
