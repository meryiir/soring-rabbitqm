package com.example.demo.producer2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMessageDto {
	private String userId;
	private String name;
	private String email;
	private String timestamp;
}
