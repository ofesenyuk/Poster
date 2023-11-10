package com.sf.poster.entity

import org.springframework.data.annotation.Id

/**
 *
 * @author OFeseniuk
 */
class Customer {
    @Id
    Long id;
    String name;
    Set<Long> subscriptionsIds;
    
    @Override
    String toString() {
        "$id:{id} name:${name}"
    }
}

