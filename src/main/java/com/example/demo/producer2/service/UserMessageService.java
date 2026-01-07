package com.example.demo.producer2.service;

import com.example.demo.producer2.config.RabbitMQConfig;
import com.example.demo.producer2.model.UserMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserMessageService {

	private final RabbitTemplate rabbitTemplate;

	public void sendUserMessage(UserMessageDto userMessageDto) {
		log.info("Envoi du message User: {}", userMessageDto);
		rabbitTemplate.convertAndSend(
				RabbitMQConfig.EXCHANGE_NAME,
				RabbitMQConfig.ROUTING_KEY,
				userMessageDto
		);
		log.info("Message User envoyé avec succès à RabbitMQ");
	}
}
