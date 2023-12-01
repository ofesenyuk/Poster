package com.sf.poster.controller

import com.sf.poster.dto.CustomerCreateDto
import com.sf.poster.dto.CustomerSubscriberDto
import com.sf.poster.dto.PostDto
import com.sf.poster.entity.Customer
import com.sf.poster.entity.Post
import com.sf.poster.repository.CustomerRepository
import com.sf.poster.repository.PostRepository
import com.sf.poster.service.CustomerService
import jakarta.servlet.ServletException
import org.hamcrest.Matchers
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 *
 * @author oleksandr
 */
class CustomerControllerTest extends Specification {

    CustomerRepository customerRepository = Stub(CustomerRepository)
    PostRepository postRepository = Stub(PostRepository)

    CustomerService customerService = new CustomerService(customerRepository, postRepository)
    CustomerController customerController = new CustomerController(customerService)

    MockMvc mockMvc

    void setup() {
        customerRepository = Stub(CustomerRepository)
        postRepository = Stub(PostRepository)

        customerService = new CustomerService(customerRepository, postRepository)
        customerController = new CustomerController(customerService)

        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build()
    }

    def "when post is performed then the response has status 200 and #customer is in content"() {
        given:
        customerRepository.save(_ as Customer) >> { arguments ->
            assert arguments[0].name == customer.name
            customer
        }

        expect: "Status is 200 and the response is #customer"
        mockMvc.perform(post('/api/v1/customer')
                .contentType(MediaType.APPLICATION_JSON)
                .content(dto.toString())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.name').value(name))

        where:
        name = 'Vova'
        dto = new CustomerCreateDto(name: name)
        customer = new Customer(id: 1, name: name)
    }

    def "when post without name is performed then the response has status 400 and #customer is in content"() {
        expect: "Status is 400"
        mockMvc.perform(post("/api/v1/customer/"))
                .andDo(print())
                .andExpect(status().is(400))
    }

    def "when put is performed then the response has status 200 and updated #customer is in content"() {
        given:
        customerRepository.save(_ as Customer) >> { arguments ->
            assert arguments[0].name == customer.name
            assert arguments[0].id == customer.id
            customer
        }
        and:
        customerRepository.findById(_ as Long) >> Optional.of(customer)

        expect: "Status is 200 and the response is #customer"
        mockMvc.perform(put("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customer.toString())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.name').value(customer.name))
                .andExpect(jsonPath('$.id').value(customer.id))

        where:
        customer = new Customer(id: 1, name: 'Gena')
    }

    def "when put for customer without name is performed then the response has status 400"() {
        expect: "Status is 400"
        mockMvc.perform(put("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content('{"id": 1}'))
                .andDo(print())
                .andExpect(status().is(400))
    }

    def "when put for customer without name is performed then the response has status 400"() {
        expect: "Status is 400"
        mockMvc.perform(put("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content('{"name": "Pasha"}'))
                .andDo(print())
                .andExpect(status().is(400))
    }

    def "when put with fake customer id is performed then exception is thrown"() {
        given:
        customerRepository.findById(_ as Long) >> Optional.empty()

        when: "exception is thrown"
        mockMvc.perform(put("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customer.toString())
        )
                .andDo(print())

        then:
        def e = thrown(ServletException)
        e.message.contains("no customer with id = ${customer?.id} is found")
        where:
        customer = new Customer(id: 1, name: 'Kostya')
    }

    def "when delete is performed then the response has status 200 and deleted #customer is in content"() {
        given:
        customerRepository.delete(_ as Customer) >> { arguments ->
            assert arguments[0].name == customer.name
            assert arguments[0].id == customer.id
            customer
        }
        and:
        customerRepository.findById(_ as Long) >> {
            arguments
            assert arguments[0] == customer.id
            Optional.of(customer)
        }
        and:
        postRepository.deleteByCustomerId(_ as Long) >> {}

        expect: "Status is 200 and the response is #customer"
        mockMvc.perform(delete("/api/v1/customer/${customer.id}")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.name').value(customer.name))
                .andExpect(jsonPath('$.id').value(customer.id))

        where:
        customer = new Customer(id: 1, name: 'Gosha')
    }

    def "when delete without customer id is performed then exception is thrown"() {
        given:
        customerRepository.findById(_ as Long) >> {
            arguments
            assert arguments[0] == customer.id
            Optional.empty()
        }

        expect: "method delete is not supported"
        mockMvc.perform(delete("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().is(405))

        where:
        customer = new Customer(id: 1, name: 'Gosha')
    }

    def "when delete for absent customer is performed then exception is thrown"() {
        given:
        customerRepository.findById(_ as Long) >> {
            arguments
            assert arguments[0] == customer.id
            Optional.empty()
        }

        when: "request with illegal customer"
        mockMvc.perform(delete("/api/v1/customer/${customer.id}")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())

        then: "wrapped illegal argument exception is thrown"
        def e = thrown(ServletException)
        e.message.contains("no customer with id = ${customer?.id} is found")

        where:
        customer = new Customer(id: 1, name: 'Gosha')
    }

    def "on subscribe, status is 200 and #customer is with postCustomerId added to its subscriptionsIds"() {
        given: "check if namely input customer is saved and returned"
        customerRepository.save(_ as Customer) >> { arguments ->
            assert arguments[0].id == customerSubscriberDto.customerId
            assert customerSubscriberDto.postCustomerId in arguments[0].subscriptionsIds
            customer.subscriptionsIds << customerSubscriberDto.postCustomerId
            customer
        }

        and: "check if customer ids from input are in parameters, then customer with the corresponding id is returned"
        customerRepository.findById(_ as Long) >> { arguments ->
            def id = arguments[0] as Long
            assert id in [customerSubscriberDto.customerId, customerSubscriberDto.postCustomerId]
            Optional.of(new Customer(id: id))
        }

        expect: "Status is 200 and the response is #customer"
        mockMvc.perform(post("/api/v1/customer/subscribe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerSubscriberDto.toString())
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.name').value(customer.name))
                .andExpect(jsonPath('$.id').value(customer.id))
                .andExpect(jsonPath('$.subscriptionsIds')
                        .value(Matchers.hasItems(customerSubscriberDto.postCustomerId.intValue())))

        where:
        customerSubscriberDto = new CustomerSubscriberDto(customerId: 1L, postCustomerId: 7L)
        subscriptionsIds = Collections.singletonList(customerSubscriberDto.postCustomerId)
        customer = new Customer(id: customerSubscriberDto.customerId, name: 'Natasha', subscriptionsIds: [19L])
    }

    def "when subscribe with empty customerId is performed then the response has status 405"() {
        expect: "Status is 405 - bad request"
        mockMvc.perform(post("/api/v1/customer/subscribe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerSubscriberDto.toString())
        )
        where:
        customerSubscriberDto = new CustomerSubscriberDto(postCustomerId: 7L)
    }

    def "when subscribe with empty postCustomerId is performed then the response has status 405"() {
        expect: "Status is 405 - bad request"
        mockMvc.perform(post("/api/v1/customer/subscribe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerSubscriberDto.toString())
        )
        where:
        customerSubscriberDto = new CustomerSubscriberDto(customerId: 7L)
    }

    def "when subscribe with customerId = postCustomerId is performed IllegalAccessException is thrown"() {
        when: "IllegalAccessException wrapped into ServletException"
        mockMvc.perform(post("/api/v1/customer/subscribe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerSubscriberDto.toString())
        )

        then:
        def e = thrown(ServletException)
        assert e.getCause() instanceof IllegalAccessException
        e.message.contains("customerId = postCustomerId = ${customerSubscriberDto?.customerId}")

        where:
        customerSubscriberDto = new CustomerSubscriberDto(customerId: 7L, postCustomerId: 7L)
    }

    def "when subscribe with absent customerId is performed IllegalAccessException is thrown"() {
        given:
        customerRepository.findById(_ as Long) >> Optional.empty()

        when: "IllegalAccessException wrapped into ServletException"
        mockMvc.perform(post("/api/v1/customer/subscribe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerSubscriberDto.toString())
        )

        then:
        def e = thrown(ServletException)
        assert e.getCause() instanceof IllegalArgumentException
        e.message.contains("no customer with id = ${customerSubscriberDto?.customerId} is found")

        where:
        customerSubscriberDto = new CustomerSubscriberDto(customerId: 7L, postCustomerId: 17L)
    }

    def "when subscribe with absent postCustomerId is performed IllegalAccessException is thrown"() {
        given:
        customerRepository.findById(_ as Long) >> { arg ->
            arg[0] == customerSubscriberDto.customerId
                    ? Optional.of(new Customer(id: customerSubscriberDto.customerId))
                    : Optional.empty()
        }

        when: "IllegalAccessException wrapped into ServletException"
        mockMvc.perform(post("/api/v1/customer/subscribe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerSubscriberDto.toString())
        )

        then:
        def e = thrown(ServletException)
        assert e.getCause() instanceof IllegalArgumentException
        e.message.contains("no customer with id = ${customerSubscriberDto?.postCustomerId} is found")

        where:
        customerSubscriberDto = new CustomerSubscriberDto(customerId: 7L, postCustomerId: 17L)
    }

    def "on unsubscribe, status is 200 and #customer without postCustomerId in to its subscriptionsIds is returned"() {
        given: "check if namely input customer is saved and returned"
        customerRepository.save(_ as Customer) >> { arguments ->
            assert arguments[0].id == customerSubscriberDto.customerId
            arguments[0]
        }

        and: "check if customer ids from input are in parameters, then customer with the corresponding id is returned"
        customerRepository.findById(_ as Long) >> { arguments ->
            def id = arguments[0] as Long
            assert id in [customerSubscriberDto.customerId, customerSubscriberDto.postCustomerId]
            def subIds = id == customer.id ? customer.subscriptionsIds : [] as Set
            Optional.of(new Customer(id: id, name: customer.name, subscriptionsIds: subIds))
        }

        expect: "Status is 200 and the response is #customer"
        mockMvc.perform(delete("/api/v1/customer/unsubscribe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerSubscriberDto.toString())
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.name').value(customer.name))
                .andExpect(jsonPath('$.id').value(customer.id))
                .andExpect(jsonPath('$.subscriptionsIds')
                        .value(Matchers.hasSize(1)))

        where:
        customerSubscriberDto = new CustomerSubscriberDto(customerId: 1L, postCustomerId: 7L)
        ids = [customerSubscriberDto.postCustomerId, 19L] as Set
        customer = new Customer(id: customerSubscriberDto.customerId, name: 'Natasha', subscriptionsIds: ids)
    }

    def "on unsubscribe with empty customerId bad request is returned"() {
        expect: "Status is 400 - bad request"
        mockMvc.perform(delete("/api/v1/customer/unsubscribe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerSubscriberDto.toString())
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(400))

        where:
        customerSubscriberDto = new CustomerSubscriberDto(postCustomerId: 7L)
    }

    def "on unsubscribe with empty postCustomerId bad request is returned"() {
        expect: "Status is 400 - bad request"
        mockMvc.perform(delete("/api/v1/customer/unsubscribe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerSubscriberDto.toString())
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(400))

        where:
        customerSubscriberDto = new CustomerSubscriberDto(postCustomerId: 7L)
    }

    def "when unsubscribe with customerId = postCustomerId is performed IllegalAccessException is thrown"() {
        when: "IllegalAccessException wrapped into ServletException"
        mockMvc.perform(delete("/api/v1/customer/unsubscribe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerSubscriberDto.toString())
        )

        then:
        def e = thrown(ServletException)
        assert e.getCause() instanceof IllegalAccessException
        e.message.contains("customerId = postCustomerId = ${customerSubscriberDto?.customerId}")

        where:
        customerSubscriberDto = new CustomerSubscriberDto(customerId: 7L, postCustomerId: 7L)
    }

    def "when unsubscribe with absent customerId is performed IllegalAccessException is thrown"() {
        given:
        customerRepository.findById(_ as Long) >> Optional.empty()

        when: "IllegalAccessException wrapped into ServletException"
        mockMvc.perform(delete("/api/v1/customer/unsubscribe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerSubscriberDto.toString())
        )

        then:
        def e = thrown(ServletException)
        assert e.getCause() instanceof IllegalArgumentException
        e.message.contains("no customer with id = ${customerSubscriberDto?.customerId} is found")

        where:
        customerSubscriberDto = new CustomerSubscriberDto(customerId: 7L, postCustomerId: 17L)
    }

    def "when unsubscribe with absent postCustomerId is performed IllegalAccessException is thrown"() {
        given:
        customerRepository.findById(_ as Long) >> { arg ->
            arg[0] == customerSubscriberDto.customerId
                    ? Optional.of(new Customer(id: customerSubscriberDto.customerId))
                    : Optional.empty()
        }

        when: "IllegalAccessException wrapped into ServletException"
        mockMvc.perform(delete("/api/v1/customer/unsubscribe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerSubscriberDto.toString())
        )

        then:
        def e = thrown(ServletException)
        assert e.getCause() instanceof IllegalArgumentException
        e.message.contains("no customer with id = ${customerSubscriberDto?.postCustomerId} is found")

        where:
        customerSubscriberDto = new CustomerSubscriberDto(customerId: 7L, postCustomerId: 17L)
    }

    def "on get comments by customerId, status is 200 and postDtoList is returned"() {
        given: "check if customer id from input is in parameters, then posts with the corresponding customerId are returned"
        postRepository.getByCustomerIdOrderByDate(_ as Long) >> { arguments ->
            assert arguments[0] == customerId
            posts
        }

        and: "check if customer id from input is in parameters, then customer with the corresponding id is returned"
        customerRepository.findById(_ as Long) >> { arguments ->
            assert arguments[0] == customerId
            Optional.of(new Customer(id: customerId, name: "customer.name"))
        }

        expect: "Status is 200 and the response is postDtoList"
        def response = mockMvc.perform(get("/api/v1/customer/comments/${customerId}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn()
                .response
        assert response.contentAsString == "[${postDtoList.join(',')}]"

        where:
        customerId = 1L
        posts = [new Post(id: 1L, content: 'Post 1', customerId: customerId, date: new Date()),
                 new Post(id: 2L, content: 'Post 2', customerId: customerId, date: new Date())]
        postDtoList = posts.collect {
            new PostDto(id: it.id, customerId: it.customerId, text: it.content, date: it.date, likerIds: it.likerIds)
        }
    }

    def "on get comments by absent customerId IllegalArgumentException is thrown"() {
        given: "customerId is absent"
        customerRepository.findById(_ as Long) >> Optional.empty()

        when: "exception is thrown"
        mockMvc.perform(get("/api/v1/customer/comments/${customerId}"))

        then:
        def e = thrown(ServletException)
        assert e.getCause() instanceof IllegalArgumentException
        e.message.contains("no customer with id = ${customerId} is found")

        where:
        customerId = 1L
    }

    def "on get comments of subscribers of given customerId, status is 200 and posts are returned"() {
        given: "check if customer id from input is in parameters, then customer with the corresponding id is returned"
        postRepository.getByCustomerIdInOrderByDate(_ as Collection<Long>) >> { arguments ->
            assert arguments[0] == subscriptionsIds
            posts
        }

        and: "check if customer id from input is in parameters, then customer with the corresponding id is returned"
        customerRepository.findById(_ as Long) >> { arguments ->
            assert arguments[0] == customerId
            Optional.of(new Customer(id: customerId, name: "This Customer", subscriptionsIds: subscriptionsIds))
        }

        expect: "Status is 200 and the response is postDtoList"
        def response = mockMvc
                .perform(get("/api/v1/customer/subscribed/comments/${customerId}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn()
                .response
        assert response.contentAsString == "[${postDtoList.join(',')}]"

        where:
        customerId = 1L
        posts = [new Post(id: 1L, content: 'Post 1', customerId: 2L, date: new Date()),
                 new Post(id: 2L, content: 'Post 2', customerId: 3L, date: new Date()),
                 new Post(id: 3L, content: 'Post 3', customerId: 4L, date: new Date())]
        subscriptionsIds = posts.collect{it.customerId} as Set<Long>
        postDtoList = posts.collect {
            new PostDto(id: it.id, customerId: it.customerId, text: it.content, date: it.date, likerIds: it.likerIds)
        }
    }

    def "on get comments by absent customerId IllegalArgumentException is thrown"() {
        given: "customerId is absent"
        customerRepository.findById(_ as Long) >> Optional.empty()

        when: "exception is thrown"
        mockMvc.perform(get("/api/v1/customer/subscribed/comments/${customerId}"))

        then:
        def e = thrown(ServletException)
        assert e.getCause() instanceof IllegalArgumentException
        e.message.contains("no customer with id = ${customerId} is found")

        where:
        customerId = 1L
    }

}

