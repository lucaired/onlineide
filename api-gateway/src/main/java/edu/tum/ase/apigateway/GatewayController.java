package edu.tum.ase.apigateway;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller // Use @Controller instead of @RestController to enable the forwarding
public class GatewayController {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String index() {
        return "forward:/ui/";
    }

}
