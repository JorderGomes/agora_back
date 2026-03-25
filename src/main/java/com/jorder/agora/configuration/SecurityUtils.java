package com.jorder.agora.configuration;

import com.jorder.agora.exceptions.ForbiddenActionException;
import com.jorder.agora.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityUtils {

    public static UUID getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ForbiddenActionException("Usuário não autenticado");
        }

        User user = (User) authentication.getPrincipal();
        assert user != null;
        return user.getId();
    }

}
