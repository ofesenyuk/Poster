package com.sf.poster.controller

import com.sf.poster.entity.Customer
import com.sf.poster.service.CustomerService
import com.sf.poster.dto.CustomerSubscriberDto
import com.sf.poster.dto.CustomerDto
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
import org.springframework.validation.BindingResult;
import org.springframework.http.HttpStatus

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
    ResponseEntity<?> addCustomer(@PathVariable String name) {
        new ResponseEntity(service.addCustomer(name), HttpStatus.OK);
    }
    
    @PutMapping
    ResponseEntity<?> updateCustomer(@Valid @RequestBody CustomerDto c) {
        new ResponseEntity(service.update(c), HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    Customer deleteCustomer(@PathVariable long id) {
        Customer c = service.findById(id).orElseThrow{new IllegalArgumentException("no customer is found")}
        service.delete(с);
        c;
    }
    
    @PostMapping("/subscribe")
    ResponseEntity<?> subscribeToCustomer(@RequestBody CustomerSubscriberDto dto) {
        if (!dto?.customerId) {
            throw new IllegalArgumentException("(current customer) customerId is absent");
        }
        if (!dto.postCustomerId) {
            throw new IllegalArgumentException("(Customer to subsribe) postCustomerId is absent");

        }
        if (!service.findById(dto.customerId) || !service.findById(dto.postCustomerId)) {
            return new ResponseEntity<>("no customer is found", HttpStatus.NOT_FOUND) 
        }
        new ResponseEntity<>(service.subscribeToCustomer(dto), HttpStatus.OK);   
    }
    
    @PostMapping("/unsubscribe")
    ResponseEntity<?> unsubscribeFromCustomer(@RequestBody CustomerSubscriberDto dto) {
        if (!service.findById(dto.customerId) || !service.findById(dto.postCustomerId)) {
            return new ResponseEntity<>("no customer is found", HttpStatus.NOT_FOUND) 
        }
        new ResponseEntity<>(service.unsubscribeFromCustomer(dto), HttpStatus.OK);   
    }    
    
    @GetMapping("/comments/{customerId}")
    ResponseEntity<?> getCustomerComments(@PathVariable long customerId) {
        Customer c = service.findById(customerId);
        if (!c) {
            return new ResponseEntity<>("customer is not found", HttpStatus.NOT_FOUND);
        }
        new ResponseEntity(service.getCustomerComments(customerId), HttpStatus.OK)
    }
    
    @GetMapping("/customer/subscribed/comments")
    ResponseEntity<?> getCustomerSubscribedComments(long customerId) {
        Customer c = service.findById(customerId);
        if (!c) {
            return new ResponseEntity<>("customer is not found", HttpStatus.NOT_FOUND);
        }
        new ResponseEntity(service.getCustomerSubscribedComments(customerId), HttpStatus.OK)
    }
}

