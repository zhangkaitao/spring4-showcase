import com.sishuok.spring4.aop.MyAspect
import com.sishuok.spring4.xml.MessagePrinter
import com.sishuok.spring4.xml.MessageServiceImpl

beans {
    messageService(MessageServiceImpl) {
        message = "hello"
    }

    messagePrinter(MessagePrinter, messageService)

    xmlns aop: "http://www.springframework.org/schema/aop"
    myAspect(MyAspect)
    aop {
        config("proxy-target-class": true) {
            aspect(id: "test", ref: "myAspect") {
                before method: "before", pointcut: "execution(void com.sishuok.spring4..*.*(..))"
            }
        }
    }


    xmlns util: "http://www.springframework.org/schema/util"
    util.map(id: 'map') {
        entry(key: 1, value: 1)
        entry('key-ref': "messageService", 'value-ref': "messageService")
    }

}