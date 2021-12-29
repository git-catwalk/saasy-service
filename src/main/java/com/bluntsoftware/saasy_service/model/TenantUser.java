package com.bluntsoftware.saasy_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class TenantUser {
    @Id
    private String id;
    private String tenantId;
    private String email;
    private String name;
    private Boolean active;
    private Boolean isCustomer;
    private List<String> roles;
}
