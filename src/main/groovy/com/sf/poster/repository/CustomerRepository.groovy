package com.sf.poster.repository

import com.sf.poster.entity.Customer
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
//import org.springframework.data.rest.core.annotation.RepositoryRestResource

/**
 *
 * @author OFeseniuk
 */
//@RepositoryRestResource(collectionResourceRel = "customer", path = "customer")
interface CustomerRepository extends MongoRepository<Customer, Long> {
    @Query('''
    db.myCollection.aggregate({
        $group: {
            _id: '',
            last: {
                $max: "$_id"
            }
        }
    });
    ''')
    Long getCustomersWithMaxId();
    
    List<Customer> findTopByOrderByIdDesc();
}

