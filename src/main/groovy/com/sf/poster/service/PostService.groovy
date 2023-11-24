package com.sf.poster.service

import com.sf.poster.entity.Post
import com.sf.poster.dto.PostDto
import com.sf.poster.dto.PostCreateDto
import com.sf.poster.dto.PostUpdateDto
import com.sf.poster.dto.PostCommentDto
import com.sf.poster.dto.CustomerLikePostDto
import com.sf.poster.repository.PostRepository
import org.springframework.stereotype.Service

/**
 *
 * @author OFeseniuk
 */
@Service
class PostService {
    PostRepository repository;
    
    PostService(PostRepository repository) {
        this.repository = repository;
    }
    
    Post findById(Long id) {
        repository.findById(id)
            .orElseThrow{new IllegalArgumentException(
                    "post with id = ${p.id} is not found")};
    }
        
    Post addPost(PostCreateDto p) {
        repository.save(new Post(id: getNewId(), customerId: p.customerId, content: p.text, date: new Date()));
    }
        
    Post updatePost(PostUpdateDto p) {
        Post post = this.findById(p.id);
        post.content = p.text;
        repository.save(post);
    }
    
    Post addComment(PostCommentDto p) {
        Post post = this.findById(p.id);
        PostCreateDto postCreateDto = new PostCreateDto(customerId: p.customerId, text: p.comment);
        Post newPost = this.addPost(postCreateDto);
        newPost.parentPostId = post.id;
        repository.save(newPost);
    }
    
    Post addCustomerLikeToPost(CustomerLikePostDto like) {
        Post post = this.findById(like.id);
        post.likerIds.add(like.customerId);
        repository.save(post);
    }
    
    Post removeCustomerLikeFromPost(CustomerLikePostDto like) {
        Post post = this.findById(like.id);
        post.likerIds.remove(like.customerId);
        repository.save(post);
    }
    
    private Long getNewId() {
        List<Post> posts = repository.findTopByOrderByIdDesc()
        posts ? posts[0]?.id + 1 : 1;
    }
}

