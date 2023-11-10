package com.sf.poster.entity

import org.springframework.data.annotation.Id

/**
 *
 * @author OFeseniuk
 */
class Post {
	@Id
        Long id;
        Date date;
        String content;
        Long customerId;
        Long parentPostId;
        Set<Long> likerIds;
        List<Post> comments;
}

