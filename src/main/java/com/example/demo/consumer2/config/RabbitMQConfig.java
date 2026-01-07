package com.example.demo.consumer2.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	// Noms des éléments RabbitMQ (peuvent être différents du mini-projet 1)
	public static final String EXCHANGE_NAME = "demo.user.exchange";
	public static final String QUEUE_NAME = "demo.user.queue";
	public static final String ROUTING_KEY = "demo.user.routingkey";

	// Déclaration dynamique de l'Exchange
	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(EXCHANGE_NAME);
	}

	// Déclaration dynamique de la Queue
	@Bean
	public Queue queue() {
		return QueueBuilder.durable(QUEUE_NAME).build();
	}

	// Déclaration dynamique du Binding
	@Bean
	public Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder
				.bind(queue)
				.to(exchange)
				.with(ROUTING_KEY);
	}

	// Configuration du MessageConverter pour JSON
	// Note: Jackson2JsonMessageConverter est déprécié mais fonctionne toujours
	// Pour Spring Boot 4.0+, considérer MappingJackson2MessageConverter à l'avenir
	@Bean
	@SuppressWarnings("deprecation")
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}
