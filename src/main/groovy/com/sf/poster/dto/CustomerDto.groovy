package com.sf.poster.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.NotBlank

/**
 *
 * @author OFeseniuk
 */
class CustomerDto {
    @NotNull
    Long id
    @NotBlank
    String name

    @Override
    String toString() {
        "{\"id\": ${id}, \n \"name\": \"${name}\"}"
    }
}

