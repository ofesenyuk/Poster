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
    final CustomerRepository customerRepository;
    final PostRepository postRepository;
    
    CustomerService(CustomerRepository customerRepository, PostRepository postRepository) {
        this.customerRepository = customerRepository;
        this.postRepository = postRepository;
    }
    
    Optional<Customer> findById(Long id) {
        customerRepository.findById(id);
    }
    
    Customer addCustomer(String name) {        
        Customer c = new Customer(name: name, id: getNewId());
        customerRepository.save(c)
    }
        
    CustomerDto update(CustomerDto c) {
        Customer newCustomer = customerRepository.save(new Customer(id: c.id, name: c.name));
        new CustomerDto(id: newCustomer.id, name: newCustomer.name)
    }
    
    void delete(Customer c) {
        postRepository.deleteByCustomerId(c.id)
        customerRepository.delete(c);
    }
    
    Customer subscribeToCustomer(CustomerSubscriberDto dto) {
        Customer subscriber = customerRepository.findById(dto.postCustomerId).get();
        subscriber.subscriptionsIds.add(dto.customerId);
        customerRepository.save(c);
    }
    
    Customer unsubscribeFromCustomer(CustomerSubscriberDto dto) {
        Customer subscriber = customerRepository.findById(dto.postCustomerId).get();
        subscriber.subscriptionsIds.remove(dto.customerId);
        customerRepository.save(c);
    }
    
    List<PostDto> getCustomerComments(long id) {
        postRepository.getByCustomerIdOrderByDate(id).collect{new PostDto(customerId: customerId, text: content, date: date, likerIds: likerIds)}
    }
    
    List<PostDto> getCustomerSubscribedComments(long id) {
        Customer c = customerRepository.findById(id).get();
        postRepository.getByCustomerIdInOrderByDate(c.subscriptionsIds).collect{new PostDto(customerId: customerId, text: content, date: date, likerIds: likerIds)}
    }
    
    private Long getNewId() {
        List<Customer> customers = customerRepository.findTopByOrderByIdDesc()
        customers ? customers[0].id + 1 : 1;
    }
        
}

