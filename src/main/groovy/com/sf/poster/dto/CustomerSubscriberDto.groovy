package com.sf.poster.dto

import jakarta.validation.constraints.NotNull;

/**
 *
 * @author OFeseniuk
 */
class CustomerSubscriberDto {
    @NotNull
    long customerId;
    @NotNull
    long postCustomerId;
}

