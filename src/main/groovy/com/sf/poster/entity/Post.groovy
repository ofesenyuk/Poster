package com.sf.poster.entity

import org.springframework.data.annotation.Id
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

/**
 *
 * @author OFeseniuk
 */
class Post {
	@Id
        Long id;
        @NotNull
        Date date;
        @NotBlank
        String content;
        @NotNull
        Long customerId;
        Long parentPostId;
        Set<Long> likerIds = new HashSet<>();
        List<Post> comments = new ArrayList<>();
}

