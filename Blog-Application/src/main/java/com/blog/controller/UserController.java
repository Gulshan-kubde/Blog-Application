package com.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.blog.entity.Comment;
import com.blog.entity.Post;
import com.blog.entity.User;
import com.blog.repository.UserRepository;
import com.blog.service.CommentService;
import com.blog.service.PostService;
import com.blog.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    
    
    

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("name", "Gulshan");
        return "Home_page";
    }

    @GetMapping("/signup")
    public String signUpPage(Model model) {
//        model.addAttribute("user", new User());
        return "SignUp_page";
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute User user, Model model) {
        userService.saveUser(user);
        model.addAttribute("message", "User signed up successfully!");
        return "redirect:/home"; // Redirect to home page after successful sign-up
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
//        model.addAttribute("user", new User());
        return "Login_page";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user) {
        boolean isValidUser = userService.validateUser(user.getUsername(), user.getPassword());
        if (isValidUser) {
        	User user1 = userRepository.findByUsername(user.getUsername());
        	System.out.println(user1.getId()+user1.getUsername());
            return "redirect:/blog?id=" + user1.getId();
        } else {
            return "Login_page";
        }
    }

    @GetMapping("/blog")
    public String blogApplication(@RequestParam("id") Long id, Model model) {
    	User user=userService.getUserById(id);
        model.addAttribute("username", user.getUsername());
        System.out.println(user.getId()+"id h");
        model.addAttribute("id", user.getId());
        List<Post> posts=postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "blog_application";
    }
    

    @PostMapping("/post")
  public String createNewPost(@RequestParam("id") Long id,@RequestParam("title") String title,@RequestParam("content") String content) {
  	System.out.println("post methon working"+title+content+id);
  	 Post post = new Post();
  	post.setTitle(title);
  	post.setContent(content);
  	User user=userService.getUserById(id);
  	post.setUser(user);
  	postService.savePost(post);

      return "redirect:/blog?id=" + id;
  }
    @PostMapping("/comment")
    public String addComment(@RequestParam("postId") Long postId, @RequestParam("comment") String content, @RequestParam("userId") Long userId) {
        Post post = postService.getPostById(postId);
        User user = userService.getUserById(userId);
        Comment comment = new Comment();
        comment.setComment(content);
        comment.setPost(post);
        comment.setUser(user);
        commentService.saveComment(comment);
        System.out.println(comment.getComment());
        return "redirect:/blog?id=" + userId;
    }
    @GetMapping("/comment")
    public String showPostWithComments(@RequestParam("postId") Long postId,@RequestParam("userId") Long userId, Model model) {
    
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        Post post =postService.getPostbyPostId(postId);
        if(post==null) {
        	return "Home_page";
        }
        model.addAttribute("post", post);
        model.addAttribute("userId", userId);
      
        System.out.println(comments);
        model.addAttribute("comments", comments);
        
        return "comment"; 
    }
    
    @PostMapping("/comments")
    public String editPost(@RequestParam("postId") Long postId, @RequestParam("userId") Long userId,@RequestParam("title") String title,@RequestParam("content") String content) {
      
        System.out.println("Post edited successfully");
        Post post =postService.getPostbyPostId(postId);
        post.setTitle(title);
        post.setContent(content);
        postService.updatePost(post);
        
//        postService.editPost(postId, editedPost);
//       
        return "redirect:/user/posts?userId=" + userId;
    }

    
    @GetMapping("/user/posts")
    public String getUserPosts(@RequestParam("userId") Long userId, Model model) {
  
        List<Post> userPosts = postService.getPostsByUserId(userId);
       
        
        User user =userService.getUserById(userId);
        model.addAttribute("username", user.getUsername());

      
        model.addAttribute("posts", userPosts);
        model.addAttribute("userId", user.getId());

   
        return "user_posts";
    }
    
    @DeleteMapping("/comment")
    public ResponseEntity<String> deletePost(@RequestParam("postId") Long postId, @RequestParam("userId") Long userId) {
        try {
           
            List<Comment> comments = commentService.getCommentsByPostId(postId);
            for (Comment comment : comments) {
                commentService.deleteCommentById(comment.getId());
            }

            
            postService.deletePostById(postId);

           
            return ResponseEntity.ok("Post deleted successfully");
        } catch (Exception e) {
    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting post: " + e.getMessage());
        }
    }



}
