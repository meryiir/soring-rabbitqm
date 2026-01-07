package com.example.demo.producer2.controller;

import com.example.demo.producer2.model.UserMessageDto;
import com.example.demo.producer2.service.UserMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserMessageController {

	private final UserMessageService userMessageService;

	@PostMapping
	public ResponseEntity<String> sendUserMessage(@RequestBody UserMessageDto userMessageDto) {
		// Générer un userId et un timestamp si non fournis
		if (userMessageDto.getUserId() == null || userMessageDto.getUserId().isEmpty()) {
			userMessageDto.setUserId(UUID.randomUUID().toString());
		}
		if (userMessageDto.getTimestamp() == null || userMessageDto.getTimestamp().isEmpty()) {
			userMessageDto.setTimestamp(LocalDateTime.now()
					.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		}

		userMessageService.sendUserMessage(userMessageDto);
		return ResponseEntity.ok("Message User envoyé avec succès: " + userMessageDto.getUserId());
	}

	@GetMapping("/test")
	public ResponseEntity<String> sendTestUserMessage() {
		UserMessageDto testUser = new UserMessageDto(
				UUID.randomUUID().toString(),
				"John Doe",
				"john.doe@example.com",
				LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
		);
		userMessageService.sendUserMessage(testUser);
		return ResponseEntity.ok("Message User de test envoyé: " + testUser.getUserId());
	}
}
