package com.sf.poster.controller

import com.sf.poster.entity.Customer
import com.sf.poster.service.CustomerService
import com.sf.poster.dto.CustomerSubscriberDto
import com.sf.poster.dto.CustomerDto
import com.sf.poster.dto.PostDto
import org.springframework.http.ResponseEntity
//import org.springframework.data.rest.webmvc.RepositoryRestController
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import jakarta.validation.Valid;

/**
 *
 * @author OFeseniuk
 */

//@RepositoryRestController
@RestController
@RequestMapping("/api/v1/customer")
class CustomerController {
    final CustomerService service;
    
    CustomerController(CustomerService service) {
        this.service = service;
    }
    
    // TODO refactor with ControllerAdvice
    @PostMapping("/{name}")
    Customer addCustomer(@PathVariable String name) {
        service.addCustomer(name);
    }
    
    @PutMapping
    Customer updateCustomer(@Valid @RequestBody CustomerDto c) {
        service.update(c);
    }
    
    @DeleteMapping("/{id}")
    Customer deleteCustomer(@PathVariable long id) {
        Customer c = service.findById(id).orElseThrow{new IllegalArgumentException("no customer is found")}
        service.delete(c);
        c;
    }
    
    @PostMapping("/subscribe")
    Customer subscribeToCustomer(@Valid @RequestBody CustomerSubscriberDto dto) {
        validateSubscription(dto);
        service.subscribeToCustomer(dto);
    }
    
    @PostMapping("/unsubscribe")
    Customer unsubscribeFromCustomer(@Valid @RequestBody CustomerSubscriberDto dto) {
        validateSubscription(dto);
        service.unsubscribeFromCustomer(dto);
    }    
    
    @GetMapping("/comments/{customerId}")
    List<PostDto> getCustomerComments(@PathVariable long customerId) {
        validateCustomerId(customerId);
        service.getCustomerComments(customerId);
    }
    
    @GetMapping("/subscribed/comments/{customerId}")
    List<PostDto> getCustomerSubscribedComments(@PathVariable long customerId) {
        validateCustomerId(customerId);
        service.getCustomerSubscribedComments(customerId);
    }
    
    private validateSubscription(CustomerSubscriberDto dto) {
        if (dto.customerId == dto.postCustomerId) {
            throw new IllegalAccessException("customerId = postCustomerId = ${dto.customerId}")
        }
        Long customerAbsentId 
            = !service.findById(dto.customerId).isPresent() ? dto.customerId 
            : !service.findById(dto.postCustomerId).isPresent() ? dto.postCustomerId 
            : null;
        if (customerAbsentId) {
            throw new IllegalArgumentException("no customer with id ${customerAbsentId} is found");
        }       
    }
    
    private validateCustomerId(long customerId) {
        service.findById(customerId).orElseThrow(() -> new IllegalArgumentException("customer with id ${customerId} is not found"));
    }
}

