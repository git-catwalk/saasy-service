package com.bluntsoftware.saasy_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class App {

	@Id
	private String id;
	private String owner;
	private String name;
	private String jwkSetUri;
	private List<Plan> plans;
	private List<String> roles;
	private BraintreeCredentials braintree;
}
