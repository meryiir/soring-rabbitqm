package com.example.demo.consumer2.service;

import com.example.demo.consumer2.model.User;
import com.example.demo.consumer2.model.UserMessageDto;
import com.example.demo.consumer2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserConsumerService {

	private final UserRepository userRepository;

	@RabbitListener(queues = "demo.user.queue")
	@Transactional
	public void consumeUserMessage(UserMessageDto userMessageDto) {
		log.info("===== MESSAGE USER REÇU =====");
		log.info("UserId: {}", userMessageDto.getUserId());
		log.info("Name: {}", userMessageDto.getName());
		log.info("Email: {}", userMessageDto.getEmail());
		log.info("Timestamp: {}", userMessageDto.getTimestamp());
		log.info("==============================");

		try {
			// Vérifier si l'utilisateur existe déjà
			if (userRepository.findByUserId(userMessageDto.getUserId()).isPresent()) {
				log.warn("L'utilisateur avec l'ID {} existe déjà, mise à jour...", userMessageDto.getUserId());
				User existingUser = userRepository.findByUserId(userMessageDto.getUserId()).get();
				existingUser.setName(userMessageDto.getName());
				existingUser.setEmail(userMessageDto.getEmail());
				userRepository.save(existingUser);
				log.info("Utilisateur mis à jour avec succès: {}", existingUser.getId());
			} else if (userRepository.findByEmail(userMessageDto.getEmail()).isPresent()) {
				log.warn("Un utilisateur avec l'email {} existe déjà", userMessageDto.getEmail());
			} else {
				// Créer un nouvel utilisateur
				User user = new User();
				user.setUserId(userMessageDto.getUserId());
				user.setName(userMessageDto.getName());
				user.setEmail(userMessageDto.getEmail());

				User savedUser = userRepository.save(user);
				log.info("Utilisateur persisté avec succès dans MySQL - ID: {}, UserId: {}", 
						savedUser.getId(), savedUser.getUserId());
			}
		} catch (Exception e) {
			log.error("Erreur lors de la persistance de l'utilisateur: {}", e.getMessage(), e);
		}
	}
}
