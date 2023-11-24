package com.sf.poster.dto

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

/**
 *
 * @author ofese
 */
class PostUpdateDto {
    @NotNull
    Long id;
    @NotBlank
    String text;
    
}

