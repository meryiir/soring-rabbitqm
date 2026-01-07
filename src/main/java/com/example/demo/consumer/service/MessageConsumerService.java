package com.example.demo.consumer.service;

import com.example.demo.consumer.model.MessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageConsumerService {

	@RabbitListener(queues = "demo.queue")
	public void consumeMessage(MessageDto messageDto) {
		log.info("===== MESSAGE REÇU =====");
		log.info("ID: {}", messageDto.getId());
		log.info("Content: {}", messageDto.getContent());
		log.info("Timestamp: {}", messageDto.getTimestamp());
		log.info("========================");
		
		// Ici, dans le mini-projet 1, on se contente d'afficher le message
		// Dans le mini-projet 2, on ajoutera la persistance MySQL
	}
}
