package com.sf.poster

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import java.time.LocalDate

import com.sf.poster.repository.CustomerRepository
import com.sf.poster.repository.PostRepository
import com.sf.poster.service.CustomerService
import com.sf.poster.entity.Customer
import com.sf.poster.entity.Post

@SpringBootApplication
class PosterLikeTwitterApplication {

    static void main(String[] args) {
        SpringApplication.run(PosterLikeTwitterApplication, args)
    }
        
//    @Bean
    CommandLineRunner run(CustomerRepository repository,
                          CustomerService service, PostRepository postRepository) {
        { args -> {
                repository.deleteAll()
                println "TEST"
                Customer c1 = new Customer(id: 1, name: 'Vova')
                repository.save(c1)
                Customer c2 = new Customer(id: 3, name: 'Kolya')
                repository.save(c2)
                Customer c3 = new Customer(id: 4, name: 'Kostya')
                repository.save(c3)
                Customer c4 = service.addCustomer("Gosha")
                repository.save(c4)

            Post p1 = new Post(id: 1, date: new Date(), content: "Today is ${LocalDate.now().getDayOfWeek()}", customerId: c1.id)
                Post p2 = new Post(id: 2, date: new Date(), content: "I like spring", customerId: c1.id)
                postRepository.save(p1)
                postRepository.save(p2)
                println "SAVED"
            }
        }
    }
}
