package tzamsk.Soteria.Controller;

import org.springframework.web.bind.annotation.GetMapping;

public class AppController {
    @GetMapping("")
    public String homePage(){ return "index";}


}
