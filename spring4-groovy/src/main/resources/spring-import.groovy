import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
def class GroovyController {
    @RequestMapping("/groovy")
    @ResponseBody
    public String hello() {
        return "hello";
    }
}

beans {

    groovyController(GroovyController)


}


