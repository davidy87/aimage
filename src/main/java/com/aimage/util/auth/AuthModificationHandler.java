package com.aimage.util.auth;

import com.aimage.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;

@RequiredArgsConstructor
public class AuthModificationHandler {

    private final SessionRegistry sessionRegistry;

    /**
     * Spring Security 인증 객체 수정
     * 사용자 닉네임 or 비밀번호 수정 시 호출
     */
    public void updateAuth(User updatedUser)  {
        Authentication authentication = new UsernamePasswordAuthenticationToken(updatedUser, updatedUser.getPassword(), null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Spring Security session 삭제
     * 사용자 계정 삭제 시 호출
     */
    public void expireSession(Long userId) {
        sessionRegistry.getAllPrincipals()
                .stream()
                .filter(p -> p instanceof User principal && principal.getId().equals(userId))
                .forEach(p -> sessionRegistry.getAllSessions(p, false)
                        .forEach(SessionInformation::expireNow)
                );

        SecurityContextHolder.clearContext();
    }
}
