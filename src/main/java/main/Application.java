package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import main.spring.init.InitListener;
import main.util.PropertiesUtil;

/**
 * Created by fanzhe on 2017/12/11.
 */
@SpringBootApplication
public class Application {

    private static Boolean springInitInitListener = true;

    static{
        springInitInitListener = Boolean.parseBoolean(PropertiesUtil.getValue("main/config/project.properties","spring.init.InitListener"));
    }

    public static void main(String[] args) {

        //        SpringApplication.run(main.Application.class, args);
        SpringApplication springApplication =new SpringApplication(Application.class);

        if(springInitInitListener) {
            System.out.println("adding listeren main.spring.init.InitListener...");
            springApplication.addListeners(new InitListener());
        }
        springApplication.run(args);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//        return args -> {
//
//            System.out.println("Let's inspect the beans provided by Spring Boot:");
//
//            String[] beanNames = ctx.getBeanDefinitionNames();
//            Arrays.sort(beanNames);
//            for (String beanName : beanNames) {
//                System.out.println(beanName);
//            }
//
//        };
//    }



}
