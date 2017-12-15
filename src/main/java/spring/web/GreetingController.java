package spring.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.config.HadoopConfigUtils;

/**
 * Created by fanzhe on 2017/12/12.
 */
@Controller
@RequestMapping("/web")
public class GreetingController {
    @Autowired
    private HadoopConfigUtils hadoopConfigUtils;

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        System.out.println( hadoopConfigUtils.getConfiguration());
        return "greeting";
    }

}
