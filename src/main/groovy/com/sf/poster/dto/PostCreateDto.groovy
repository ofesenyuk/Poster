package com.sf.poster.dto

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

/**
 *
 * @author OFeseniuk
 */
class PostCreateDto {
    @NotNull
    Long customerId;
    @NotBlank
    String text;
        
    @Override
    String toString() {
        "customerId: ${customerId} text: ${text}"
    }
}

