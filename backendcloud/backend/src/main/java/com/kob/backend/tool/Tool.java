package com.kob.backend.tool;

import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class Tool {
    UsernamePasswordAuthenticationToken authenticationToken=
            (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl loginUser=(UserDetailsImpl)authenticationToken.getPrincipal();
    public User user=loginUser.getUser();
}
