package com.sf.poster.dto

import jakarta.validation.constraints.NotNull

/**
 *
 * @author OFeseniuk
 */
class CustomerSubscriberDto {
    @NotNull
    Long customerId
    @NotNull
    Long postCustomerId

    @Override
    String toString() {
        """
            {
            "customerId": ${customerId},
            "postCustomerId": ${postCustomerId}
            }
        """
    }
}

