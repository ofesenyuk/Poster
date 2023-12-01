package com.sf.poster.entity

import org.springframework.data.annotation.Id

/**
 *
 * @author OFeseniuk
 */
class Customer {
    @Id
    Long id
    String name
    Set<Long> subscriptionsIds = new HashSet<>()
    
    @Override
    String toString() {
        "{\"id\": ${id}, \n \"name\": \"${name}\"}"
    }

}

