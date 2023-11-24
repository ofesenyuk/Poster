package com.sf.poster.dto

import jakarta.validation.constraints.NotNull;

/**
 *
 * @author OFeseniuk
 */
class CustomerLikePostDto {
    @NotNull
    Long customerId;
    @NotNull
    Long postId;
    
    Long getId() {
        postId;
    }
}

