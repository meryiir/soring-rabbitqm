package com.example.demo.consumer2.controller;

import com.example.demo.consumer2.model.User;
import com.example.demo.consumer2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserRepository userRepository;

	@GetMapping
	public ResponseEntity<List<User>> getAllUsers() {
		return ResponseEntity.ok(userRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		Optional<User> user = userRepository.findById(id);
		return user.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/userId/{userId}")
	public ResponseEntity<User> getUserByUserId(@PathVariable String userId) {
		Optional<User> user = userRepository.findByUserId(userId);
		return user.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
}
