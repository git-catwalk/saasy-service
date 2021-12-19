package com.bluntsoftware.saasy_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	private String username;
	private String firstname;
	private String lastname;
	private String email;
	private List<String> roles;
}