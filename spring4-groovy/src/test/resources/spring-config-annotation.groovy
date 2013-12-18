beans {
    xmlns context: "http://www.springframework.org/schema/context"

    context.'component-scan'('base-package': "com.sishuok.spring4") {
        'exclude-filter'('type': "aspectj", 'expression': "com.sishuok.spring4.xml.*")
    }

    message(String, "hello") {}

}