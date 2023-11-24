package com.sf.poster.controller

import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import jakarta.validation.Valid;
import java.lang.IllegalArgumentException

import com.sf.poster.entity.Post
import com.sf.poster.entity.Customer
import com.sf.poster.service.PostService
import com.sf.poster.service.CustomerService
import com.sf.poster.dto.CustomerLikePostDto
import com.sf.poster.dto.PostDto
import com.sf.poster.dto.PostCreateDto
import com.sf.poster.dto.PostUpdateDto
import com.sf.poster.dto.PostCommentDto

/**
 *
 * @author OFeseniuk
 */

@RestController
@RequestMapping("/api/v1/post")
class PostController {
        final PostService postService;
        final CustomerService customerService;

    PostController(PostService postService, CustomerService customerService) {
        this.postService = postService;
        this.customerService = customerService;
    }

    @PostMapping
    Post addPost(@Valid @RequestBody PostCreateDto p) {
        validateCustomerPresence(p);
        postService.addPost(p);
    }
    
    @PutMapping
    Post updatePost(@Valid @RequestBody PostUpdateDto p) {
        validatePostPresence(p);
        postService.updatePost(p);
    }
    
    @PostMapping("/like")
    Post addCustomerLikeToPost(@Valid @RequestBody CustomerLikePostDto like) {
        Post post = validatePostPresence(like);
        postService.addCustomerLikeToPost(like);
    }
    
    @DeleteMapping("/like")
    Post removeCustomerLikeFromPost(
        @Valid @RequestBody CustomerLikePostDto like
    ) {
        Post post = validatePostPresence(like);
        postService.removeCustomerLikeFromPost(like);
    }
    
    @PostMapping("/add/comment")
    Post addComment(@Valid @RequestBody PostCommentDto p) {
        Post post = validatePostPresence(p);
        postService.addComment(p);
    }
    
    void validateCustomerPresence(PostCreateDto p) {
        if (!customerService.findById(p.customerId).isPresent()) {
            throw new IllegalArgumentException("customer with customerId ${p.customerId} is absent");
        }        
    }
    
    Post validatePostPresence(def p) {
        if (!p?.id) {
            throw new IllegalArgumentException("Post id should be set");
        }
        postService.findById(p.id);
    }
}

