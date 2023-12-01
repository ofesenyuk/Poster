package com.sf.poster.dto

import jakarta.validation.constraints.NotEmpty

class CustomerCreateDto {
    @NotEmpty
    String name

    @Override
    String toString() {
        "{\"name\": \"${name}\"}"
    }
}
