package spring.boot.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.config.HadoopConfigUtils;

/**
 * Created by fanzhe on 2017/12/11.
 */
@RestController
@RequestMapping("/boot")
public class HelloController {

    @Autowired
    private HadoopConfigUtils hadoopConfigUtils;
    @RequestMapping("/demo/hello")
    public String index() {
        System.out.println(hadoopConfigUtils.getConfiguration());
        return "Greetings from Spring Boot!";
    }

}
