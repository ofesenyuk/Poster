package com.sf.poster.service

import com.sf.poster.entity.Post
import com.sf.poster.dto.PostDto
import com.sf.poster.dto.PostCommentDto
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
    
    Optional<Post> findById(Long id) {
        repository.findById(id);
    }
        
    Post addPost(PostDto p) {
        repository.save(new Post(id: getNewId(), customerId: p.customerId, content: p.text, date: new Date()));
    }
        
    Post updatePost(PostDto p) {
        repository.save(new Post(id: p.id, customerId: p.customerId, content: p.text, date: p.date));
    }
    
    Post addComent(PostCommentDto p) {
        Post post = repository.findById(p.id).get();
        PostDto postdto = new PostDto(customerId: post.customerId, text: p.comment);
        Post newPost = this.addPost(postdto);
        newPost.parentPostId = post.id;
        repository.save(newPost);
    }
    
    private Long getNewId() {
        List<Post> posts = repository.findTopByOrderByIdDesc()
        posts ? posts[0]?.id + 1 : 1;
    }
}

