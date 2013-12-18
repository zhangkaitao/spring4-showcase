import com.sishuok.spring4.annotation.MessageServiceImpl
import com.sishuok.spring4.xml.MessagePrinter

beans {
    messageService(MessageServiceImpl) {
        message = "hello"
    }

    messagePrinter(MessagePrinter, messageService) {

    }

}