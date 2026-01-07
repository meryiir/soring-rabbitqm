package com.example.demo.producer.controller;

import com.example.demo.producer.model.MessageDto;
import com.example.demo.producer.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

	private final MessageService messageService;

	@PostMapping
	public ResponseEntity<String> sendMessage(@RequestBody MessageDto messageDto) {
		// Générer un ID et un timestamp si non fournis
		if (messageDto.getId() == null || messageDto.getId().isEmpty()) {
			messageDto.setId(UUID.randomUUID().toString());
		}
		if (messageDto.getTimestamp() == null || messageDto.getTimestamp().isEmpty()) {
			messageDto.setTimestamp(LocalDateTime.now()
					.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		}

		messageService.sendMessage(messageDto);
		return ResponseEntity.ok("Message envoyé avec succès: " + messageDto.getId());
	}

	@GetMapping("/test")
	public ResponseEntity<String> sendTestMessage() {
		MessageDto testMessage = new MessageDto(
				UUID.randomUUID().toString(),
				"Message de test depuis le Producer",
				LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
		);
		messageService.sendMessage(testMessage);
		return ResponseEntity.ok("Message de test envoyé: " + testMessage.getId());
	}
}
