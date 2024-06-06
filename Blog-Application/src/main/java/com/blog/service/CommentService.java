package com.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.entity.Comment;
import com.blog.repository.CommentRepository;
@Service
public class CommentService {
	@Autowired
    private CommentRepository commentRepository;

    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }
    
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }
    public void deleteCommentById(Long commentId) {
        commentRepository.deleteById(commentId);
    }

}
