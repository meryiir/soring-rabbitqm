package com.example.demo.producer.service;

import com.example.demo.producer.config.RabbitMQConfig;
import com.example.demo.producer.model.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

	private final RabbitTemplate rabbitTemplate;

	public void sendMessage(MessageDto messageDto) {
		log.info("Envoi du message: {}", messageDto);
		rabbitTemplate.convertAndSend(
				RabbitMQConfig.EXCHANGE_NAME,
				RabbitMQConfig.ROUTING_KEY,
				messageDto
		);
		log.info("Message envoyé avec succès à RabbitMQ");
	}
}
