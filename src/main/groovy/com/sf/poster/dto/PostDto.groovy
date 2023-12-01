package com.sf.poster.dto

import jakarta.validation.constraints.NotNull

/**
 *
 * @author OFeseniuk
 */
class PostDto {
    @NotNull
    Long id
    @NotNull
    Long customerId
    @NotNull
    String text
    Date date
    Set<Long> likerIds
        
    @Override
    String toString() {
        "{\"id\":${id},\"customerId\":${customerId},\"text\":\"${text}\",\"date\":${date.time},\"likerIds\":${likerIds}}"
    }
}

