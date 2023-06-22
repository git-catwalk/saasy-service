package com.bluntsoftware.saasy_service.service;

import com.bluntsoftware.saasy_service.model.Tenant;
import com.bluntsoftware.saasy_service.model.TenantUser;
import com.bluntsoftware.saasy_service.model.User;
import com.bluntsoftware.saasy_service.repository.TenantRepo;
import com.bluntsoftware.saasy_service.repository.TenantUserRepo;
import org.springframework.stereotype.Service;

@Service
public class TenantUserInfoService {
    private final TenantRepo tenantRepo;
    private final TenantUserRepo tenantUserRepo;
    private final UserInfoService userInfoService;

    public TenantUserInfoService(TenantRepo tenantRepo, TenantUserRepo tenantUserRepo, UserInfoService userInfoService) {
        this.tenantRepo = tenantRepo;
        this.tenantUserRepo = tenantUserRepo;
        this.userInfoService = userInfoService;
    }
    public boolean isTenantUser(String tenantId){
        User loggedInUser = userInfoService.getLoggedInUser();
        TenantUser tenantUser = tenantUserRepo.findByTenantIdAndEmail(tenantId,loggedInUser.getEmail()).block();
        return tenantUser != null && tenantUser.getRoles() != null &&
                (tenantUser.getRoles().contains("USER") || tenantUser.getRoles().contains("ADMIN"));
    }

    public boolean isTenantAdmin(String tenantId){
        User loggedInUser = userInfoService.getLoggedInUser();
        TenantUser tenantUser = tenantUserRepo.findByTenantIdAndEmail(tenantId,loggedInUser.getEmail()).block();
        return tenantUser != null && tenantUser.getRoles() != null && tenantUser.getRoles().contains("ADMIN");
    }

    public boolean isTenant(String tenantId){
        User loggedInUser = userInfoService.getLoggedInUser();
        Tenant tenant = this.tenantRepo.findById(tenantId).block();
        return tenant != null && tenant.getCustomer() != null && loggedInUser.getEmail().equalsIgnoreCase(tenant.getCustomer().getEmail());
    }

}
