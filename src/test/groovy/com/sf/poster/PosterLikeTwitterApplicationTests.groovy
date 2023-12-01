package com.sf.poster

import org.springframework.boot.SpringApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import spock.lang.Shared
import spock.lang.Specification

import com.sf.poster.controller.CustomerController
import com.sf.poster.controller.PostController

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

@SpringBootTest(classes = PosterLikeTwitterApplication)
@ContextConfiguration
class PosterLikeTwitterApplicationTests extends Specification {

    @Autowired(required = false)
    private CustomerController customerController
    @Autowired(required = false)
    private PostController postController

    def "when context is loaded then CustomerController is created"() {
        expect: "the CustomerController is created"
        customerController
    }
    def "when context is loaded then PostController is created"() {
        expect: "the PostController is created"
        postController
    }
}
