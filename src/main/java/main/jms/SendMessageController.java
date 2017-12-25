package main.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by fansy on 2017/12/25.
 */
@Controller
@RequestMapping("/main/jms")
public class SendMessageController {

    @Autowired
    private JmsTemplate jmsTemplate;

    @RequestMapping("/send")
    public @ResponseBody  String send(){
        // Send a message with a POJO - the template reuse the message converter
        System.out.println("Sending an email message.");
        jmsTemplate.convertAndSend("mailbox", new Email("info@example.com", "Hello"));
        return "sended!";
    }
}
