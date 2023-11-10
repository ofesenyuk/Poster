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

import com.sf.poster.entity.Post
import com.sf.poster.entity.Customer
import com.sf.poster.service.PostService
import com.sf.poster.service.CustomerService
import com.sf.poster.dto.CustomerLikePostDto
import com.sf.poster.dto.PostDto
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

    // TODO refactor with ControllerAdvice to exclude ResponseEntity from controller
    @PostMapping
    ResponseEntity<?> addPost(@RequestBody PostDto p) {
        if (!customerService.findById(p?.customerId)) {
            return new ResponseEntity<>("customer is not found",
                HttpStatus.FOUND);
        }
        new ResponseEntity(postService.addPost(p), HttpStatus.OK);
    }
    
    @PutMapping
    ResponseEntity<?> updatePost(@RequestBody PostDto p) {
        println "p ${p}"
        Post post = postService.findById(p?.id);
        ResponseEntity<?> gettersResponse = checkPostPresence(post);
        if (gettersResponse.getStatusCode() != HttpStatus.OK) {
            return gettersResponse;
        }
        new ResponseEntity(postService.updatePost(p), HttpStatus.OK);
    }
    
    @PostMapping("/like")
    ResponseEntity<?> addCustomerLikeToPost(CustomerLikePostDto like) {
        Post post = postService.findById(like.postId);
        ResponseEntity<?> gettersResponse = checkPostPresence(post);
        if (gettersResponse.getStatusCode() != HttpStatus.OK) {
            return gettersResponse;
        }
        post.likerIds.add(like.customerId);
        postService.save(p);
        gettersResponse;
    }
    
    @DeleteMapping("/like")
    ResponseEntity<?> removeCustomerLikeFromPost(CustomerLikePostDto like) {
        Post post = postService.findById(like.postId);
        ResponseEntity<?> gettersResponse = checkPostPresence(post);
        if (gettersResponse.getStatusCode() != HttpStatus.OK) {
            return gettersResponse;
        }
        post.likerIds.remove(like.customerId);
        postService.save(p);
        gettersResponse;
    }
    
    @PostMapping("/add/comment")
    ResponseEntity<?> addComent(PostCommentDto p) {
        Post post = postService.findById(p.id);
        ResponseEntity<?> gettersResponse = checkPostPresence(post);
        if (gettersResponse.getStatusCode() != HttpStatus.OK) {
            return gettersResponse;
        }
        postService.addComment(p);
    }
    
    ResponseEntity<?> checkPostPresence(Post post) {
        if (!post?.id) {
            return new ResponseEntity<>("post is not found",
                HttpStatus.NOT_FOUND);
        }
        if (!customerService.findById(post.customerId)) {
            return new ResponseEntity<>("customer is not found",
                HttpStatus.NOT_FOUND);
        }
        new ResponseEntity<>(post, HttpStatus.OK)
    }
}

