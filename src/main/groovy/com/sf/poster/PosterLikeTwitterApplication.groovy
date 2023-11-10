package com.sf.poster

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

import com.sf.poster.repository.CustomerRepository
import com.sf.poster.service.CustomerService
import com.sf.poster.entity.Customer

@SpringBootApplication
class PosterLikeTwitterApplication {

    static void main(String[] args) {
        SpringApplication.run(PosterLikeTwitterApplication, args)
    }
        
    // This method is executed but its results are not in mongo.
    // I could not find why.
    @Bean
    public CommandLineRunner run(CustomerRepository repository, CustomerService service) {
        { args -> {
                repository.deleteAll();
                println "TEST"
                Customer c1 = new Customer(id: 1, name: 'Vova1');
                repository.save(c1);
                Customer c2 = new Customer(id: 3, name: 'Kolya1');
                repository.save(c2);
                Customer c3 = new Customer(id: 4, name: 'Kostya1');
                repository.save(c2);
                service.addCustomer("Gosha");
                println "SAVED"
            };
        }
    }
}
