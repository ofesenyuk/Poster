package com.sf.poster.dto

import jakarta.validation.constraints.NotNull;

/**
 *
 * @author OFeseniuk
 */
class PostCommentDto {
    @NotNull
    Long id;
    @NotNull
    Long customerId;
    @NotNull
    String comment;
    
}

