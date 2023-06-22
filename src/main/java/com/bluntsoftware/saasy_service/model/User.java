package com.bluntsoftware.saasy_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
	private String username;
	private String name;
	private String email;
	private Boolean active;
	private List<String> roles;
}
