package com.sf.poster.service

import com.sf.poster.entity.Customer
import com.sf.poster.repository.CustomerRepository
import com.sf.poster.dto.CustomerSubscriberDto
import com.sf.poster.dto.CustomerDto
import com.sf.poster.dto.PostDto
import com.sf.poster.repository.PostRepository
import org.springframework.stereotype.Service

/**
 *
 * @author OFeseniuk
 */
@Service
class CustomerService {
    final CustomerRepository customerRepository
    final PostRepository postRepository
    
    CustomerService(CustomerRepository customerRepository, PostRepository postRepository) {
        this.customerRepository = customerRepository
        this.postRepository = postRepository
    }
    
    Customer findById(Long id) {
        customerRepository.findById(id)
            .orElseThrow{new IllegalArgumentException("no customer with id = ${id} is found")}
    }
    
    Customer addCustomer(String name) {
        Customer c = new Customer(name: name, id: getNewId())
        customerRepository.save(c)
    }
        
    CustomerDto update(CustomerDto c) {
        Customer newCustomer = customerRepository.save(new Customer(id: c.id, name: c.name))
        new CustomerDto(id: newCustomer.id, name: newCustomer.name)
    }

    Customer deleteById(long id) {
        Customer c = this.findById(id)
        postRepository.deleteByCustomerId(id)
        customerRepository.delete(c)
        c
    }
    
    Customer subscribeToCustomer(CustomerSubscriberDto dto) {
        Customer subscriber = this.findById(dto.customerId)
        subscriber.subscriptionsIds.add(dto.postCustomerId)
        customerRepository.save(subscriber)
    }
    
    Customer unsubscribeFromCustomer(CustomerSubscriberDto dto) {
        Customer subscriber = customerRepository.findById(dto.customerId).get()
        subscriber.subscriptionsIds.remove(dto.postCustomerId)
        customerRepository.save(subscriber)
    }
    
    List<PostDto> getCustomerComments(long id) {
        postRepository.getByCustomerIdOrderByDate(id).collect{new PostDto(customerId: id, text: it.content, date: it.date, likerIds: it.likerIds, id: it.id)}
    }
    
    List<PostDto> getCustomerSubscribedComments(long id) {
        Customer c = this.findById(id)
        postRepository.getByCustomerIdInOrderByDate(c.subscriptionsIds).collect{
            new PostDto(customerId: it.customerId, text: it.content, date: it.date, likerIds: it.likerIds, id: it.id)}
    }
    
    private Long getNewId() {
        List<Customer> customers = customerRepository.findTopByOrderByIdDesc()
        customers ? customers[0].id + 1 : 1
    }
        
}

