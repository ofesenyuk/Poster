package com.sf.poster.dto

import jakarta.validation.constraints.NotNull;

/**
 *
 * @author OFeseniuk
 */
class CustomerLikePostDto {
    @NotNull
    long customerId;
    @NotNull
    long postId;
}

