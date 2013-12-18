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

    xmlns lang: "http://www.springframework.org/schema/lang"

    lang.'groovy'(id: 'groovyController2', 'script-source': 'classpath:com/sishuok/spring4/controller/GroovyController2.groovy')

}


