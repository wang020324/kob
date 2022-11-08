package com.kob.backend.service.impl.utils;

import com.kob.backend.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

//实现接口

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();//返回密码
    }

    @Override
    public String getUsername() {
        return user.getUsername();//返回用户名
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;//有无因封锁而到期
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;//是否被锁定
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
