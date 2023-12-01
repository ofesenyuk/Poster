package com.sf.poster.controller

import com.sf.poster.dto.CustomerCreateDto
import com.sf.poster.entity.Customer
import com.sf.poster.service.CustomerService
import com.sf.poster.dto.CustomerSubscriberDto
import com.sf.poster.dto.CustomerDto
import com.sf.poster.dto.PostDto

//import org.springframework.data.rest.webmvc.RepositoryRestController
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import jakarta.validation.Valid

/**
 *
 * @author OFeseniuk
 */

//@RepositoryRestController
@RestController
@RequestMapping("/api/v1/customer")
class CustomerController {
    final CustomerService service

    CustomerController(CustomerService service) {
        this.service = service
    }

    @PostMapping
    Customer addCustomer(@Valid @RequestBody CustomerCreateDto customerCreateDto) {
        service.addCustomer(customerCreateDto.name)
    }

    @PutMapping
    CustomerDto updateCustomer(@Valid @RequestBody CustomerDto c) {
        service.findById(c.id)
        service.update(c)
    }

    @DeleteMapping("/{id}")
    Customer deleteCustomer(@PathVariable("id") long id) {
        service.deleteById(id)
    }

    @PostMapping("/subscribe")
    Customer subscribeToCustomer(@Valid @RequestBody CustomerSubscriberDto dto) {
        validateSubscription(dto)
        service.subscribeToCustomer(dto)
    }

    @DeleteMapping("/unsubscribe")
    Customer unsubscribeFromCustomer(@Valid @RequestBody CustomerSubscriberDto dto) {
        validateSubscription(dto)
        service.unsubscribeFromCustomer(dto)
    }

    @GetMapping("/comments/{customerId}")
    List<PostDto> getCustomerComments(@PathVariable('customerId') long customerId) {
        validateCustomerId(customerId)
        service.getCustomerComments(customerId)
    }

    @GetMapping("/subscribed/comments/{customerId}")
    List<PostDto> getCustomerSubscribedComments(@PathVariable('customerId') long customerId) {
        validateCustomerId(customerId)
        service.getCustomerSubscribedComments(customerId)
    }

    private validateSubscription(CustomerSubscriberDto dto) {
        if (dto.customerId == dto.postCustomerId) {
            throw new IllegalAccessException("customerId = postCustomerId = ${dto.customerId}")
        }
        service.findById(dto.customerId)
        service.findById(dto.postCustomerId)
    }

    private Customer validateCustomerId(long customerId) {
        service.findById(customerId)
    }
}

