package com.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blog.entity.Post;
import com.blog.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public void savePost(Post post) {
        postRepository.save(post);
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    
    public Post getPostbyPostId(Long postId) {
        // Retrieve the post from the repository
        Optional<Post> optionalPost = postRepository.findById(postId);
        
        // If the post exists, return it. Otherwise, return null (or throw an exception if desired)
        return optionalPost.orElse(null);
    }
    
    public List<Post> getPostsByUserId(Long userId) {
        // You can implement your logic here to fetch posts by user ID
        // For example, if you are using Spring Data JPA, you might have a method like this:
        return postRepository.findByUserId(userId);
    }
    
    public void deletePostById(Long postId) {
        // Check if the post exists
        if (postRepository.existsById(postId)) {
            // If the post exists, delete it
            postRepository.deleteById(postId);
        } else {
            // Handle the case where the post does not exist, throw an exception, log a message, etc.
            throw new IllegalArgumentException("Post with ID " + postId + " does not exist.");
        }
    }
    
    
    public void editPost(Long postId, Post editedPost) {
        // Retrieve the post from the database by its ID
        Optional<Post> optionalPost = postRepository.findById(postId);
        
        // Check if the post exists
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            
            // Update the fields of the existing post with the edited post data
            existingPost.setTitle(editedPost.getTitle());
            existingPost.setContent(editedPost.getContent());
            
            // Save the updated post to the database
            postRepository.save(existingPost);
        } else {
            // Handle the case where the post with the given ID doesn't exist
            throw new IllegalArgumentException("Post not found with ID: " + postId);
        }
    }
    
    public void updatePost(Post post) {
    	postRepository.save(post);
    	
    }

    // Add additional methods for updating and deleting posts as needed

}

