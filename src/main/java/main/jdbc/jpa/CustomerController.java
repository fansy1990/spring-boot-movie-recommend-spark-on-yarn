package main.jdbc.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by fanzhe on 2017/12/21.
 */
@Controller
@RequestMapping("/main/jdbc/jpa")
public class CustomerController {

    @Autowired
    private CustomerReposity customerReposity;

    @GetMapping("/add")
    public @ResponseBody String addSomeCustomers(){
        this.customerReposity.addSomeCustomers();
        return "Saved some customers!";
    }

    @GetMapping(path="/all")
    public @ResponseBody List<Customer> getAllUsers() {
        // This returns a JSON or XML with the users
        return this.customerReposity.findAll();
    }

}
