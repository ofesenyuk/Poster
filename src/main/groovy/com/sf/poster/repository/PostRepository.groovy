package com.sf.poster.repository

import com.sf.poster.entity.Customer
import com.sf.poster.entity.Post
import org.springframework.data.mongodb.repository.MongoRepository
//import org.springframework.data.rest.core.annotation.RepositoryRestResource

/**
 *
 * @author OFeseniuk
 */
//@RepositoryRestResource(collectionResourceRel = "post", path = "post")
interface PostRepository extends MongoRepository<Post, Long> {
	List<Post> getByCustomerIdOrderByDate(long customerId);
	List<Post> getByCustomerIdInOrderByDate(Collection<Long> customerIds);
	void deleteByCustomerId(long customerId);
        List<Post> findTopByOrderByIdDesc();
}

