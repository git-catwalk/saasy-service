package com.bluntsoftware.saasy_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan {
	private String name;
	private String description;
	private String planId;
	private BigDecimal monthly;
	private BigDecimal yearly;
	private List<String> features;
	private String buttonTitle;
	private String payUrl;
}
