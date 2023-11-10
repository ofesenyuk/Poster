package com.sf.poster.dto

import jakarta.validation.constraints.NotNull;

/**
 *
 * @author OFeseniuk
 */
class PostCommentDto {
    @NotNull
    long id;
    @NotNull
    String comment;
}

