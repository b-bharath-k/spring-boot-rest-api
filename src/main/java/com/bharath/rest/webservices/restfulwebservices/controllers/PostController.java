package com.bharath.rest.webservices.restfulwebservices.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.bharath.rest.webservices.restfulwebservices.entities.Post;
import com.bharath.rest.webservices.restfulwebservices.entities.User;
import com.bharath.rest.webservices.restfulwebservices.exceptions.UserNotFoundException;
import com.bharath.rest.webservices.restfulwebservices.repositories.PostRepository;
import com.bharath.rest.webservices.restfulwebservices.repositories.UserRepository;

@RestController
public class PostController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;

	@GetMapping("/users/{user_id}/posts")
	public List<Post> retrieveUserPosts(@PathVariable int user_id){
		Optional<User> userOptional = userRepository.findById(user_id);
		if(!userOptional.isPresent()) {
			throw new UserNotFoundException("User with id " + user_id + " is not found");
		}
		return userOptional.get().getPosts();
	}
	
	@PostMapping("/users/{id}/posts")
	public ResponseEntity<Object> createPost(@PathVariable int id, @RequestBody Post post) {
		Optional<User> userOptional = userRepository.findById(id);
		if(!userOptional.isPresent()) {
			throw new UserNotFoundException("User with id " + id + " is not found");
		}
		User user = userOptional.get();
		post.setUser(user);
		postRepository.save(post);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
						.path("/{id}")
						.buildAndExpand(post.getId())
						.toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/users/{user_id}/posts/{post_id}")
	public void updatePost(@PathVariable int user_id, @PathVariable int post_id, @RequestBody Post post) {
		Optional<User> userOptional = userRepository.findById(user_id);
		if(!userOptional.isPresent()) {
			throw new UserNotFoundException("User with id " + user_id + " is not found");
		}
		Optional<Post> postOptional = postRepository.findById(post_id);
		if(!postOptional.isPresent()) {
			throw new UserNotFoundException("Post with id " + post_id + " is not found");
		}
		User user = userOptional.get();
		post.setUser(user);
	}
	
	@DeleteMapping("/users/{user_id}/posts/{post_id}")
	public void deletePost(@PathVariable int user_id, @PathVariable int post_id) {
		Optional<User> user = userRepository.findById(user_id);
		if(!user.isPresent()) {
			throw new UserNotFoundException("User with id " + user_id + " is not found");
		}
		postRepository.deleteById(post_id);
	}
}
