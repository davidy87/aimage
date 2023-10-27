package com.aimage.util.config.auth.session;

import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.repository.UserRepository;
import com.aimage.util.config.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("--- In UserDetailService (loadByUsername) ---");
        log.info("email = {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 계정입니다."));

        log.info("User found = {}", user);

        return CustomUserDetails.of(user, null);
    }
}
