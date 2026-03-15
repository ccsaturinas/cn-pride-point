package com.company.cnpridepoint.security;

import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;
import io.jmix.security.role.annotation.SpecificPolicy;
import io.jmix.securityflowui.role.annotation.MenuPolicy;
import io.jmix.securityflowui.role.annotation.ViewPolicy;

@ResourceRole(name = "Full View Role", code = FullViewRole.CODE)
public interface FullViewRole {
    String CODE = "full-view-role";

    @EntityPolicy(entityName = "*", actions = {EntityPolicyAction.READ})
    @EntityAttributePolicy(entityName = "*", attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @ViewPolicy(viewIds = "*")
    @MenuPolicy(menuIds = "*")
    @SpecificPolicy(resources = "*")
    void fullViewAccess();
}