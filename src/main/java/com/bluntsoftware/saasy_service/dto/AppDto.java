package com.bluntsoftware.saasy_service.dto;

import com.bluntsoftware.saasy_service.model.Plan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppDto {
    private String id;
    private String name;
    private List<Plan> plans;
    private List<String> roles;
}
