package com.aimage.domain.user.service;

import com.aimage.domain.image.entity.Image;
import com.aimage.domain.image.repository.ImageRepository;
import com.aimage.domain.user.dto.UserDto;
import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.repository.UserRepository;
import com.aimage.util.exception.AimageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static com.aimage.constant.PageConst.PAGE_SIZE;
import static com.aimage.domain.image.dto.ImageDto.*;
import static com.aimage.domain.user.dto.UserDto.*;
import static com.aimage.util.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final ImageRepository imageRepository;

    private final PasswordEncoder passwordEncoder;

    private final SessionRegistry sessionRegistry;

    public UserResponse join(Signup signupDto) {
        // 비밀번호 재입력 일치 확인
        if (!signupDto.getPassword().equals(signupDto.getConfirmPassword())) {
            throw new AimageException(CONFIRM_PASSWORD);
        }

        String encodedPassword = passwordEncoder.encode(signupDto.getPassword());
        User user = signupDto.convertToEntity(encodedPassword);
        userRepository.save(user);

        return new UserResponse(user);
    }

    public UserResponse login(String email, String password) {
        log.info("--- In UserService (login) ---");

        User loginUser = userRepository.findByEmail(email)
                .filter(user -> user.getPassword().equals(password))
                .orElseThrow(() -> new AimageException(LOGIN_ERROR));

        return new UserResponse(loginUser);
    }

    public UserResponse findUserToResetPw(String email) {
        User userFound = userRepository.findByEmail(email)
                .orElseThrow(() -> new AimageException(PASSWORD_INQUIRE_FAILED));

        log.info("User found = {}", userFound);

        return new UserResponse(userFound);
    }

    public UserResponse updateUsername(Long id, UpdateUsername updateUsername) {
        String newUsername = updateUsername.getUsername();
        User userToUpdate = userRepository.findById(id)
                .filter(user -> !user.getUsername().equals(newUsername))
                .orElseThrow(() -> new AimageException(USERNAME_UPDATE_FAILED));

        userToUpdate.updateUsername(newUsername);

        // 새로운 인증 생성 및 추가
        updateAuth(userToUpdate);

        return new UserResponse(userToUpdate);
    }

    public UserResponse updatePassword(Long userId, UpdatePassword updatePassword) {
        String newPassword = updatePassword.getPassword();
        String confirmPassword = updatePassword.getConfirmPassword();

        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new AimageException(PASSWORD_INQUIRE_FAILED));

        if (!newPassword.equals(confirmPassword)) {
            throw new AimageException(CONFIRM_PASSWORD);
        }

        userToUpdate.updatePassword(newPassword);

        // 새로운 인증 생성 및 추가
        updateAuth(userToUpdate);

        return new UserResponse(userToUpdate);
    }

    public void deleteAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AimageException(USER_ALREADY_NOT_EXIST));

        // Spring Security session 삭제
        expireSession(userId);

        userRepository.delete(user);
    }

    public Page<ImageResponse> findSavedImages(Long userId, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, PAGE_SIZE, Sort.Direction.DESC, "id");

        return imageRepository.findAllByOwnerId(userId, pageable).map(ImageResponse::new);
    }

    public ImageResponse findByOwnerIdAndImageId(Long userId, Long imageId) {
        Image image = imageRepository.findByOwnerIdAndId(userId, imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        return new ImageResponse(image);
    }

    /**
     * 새로운 인증 생성 및 추가
     */
    private void updateAuth(User updatedUser)  {
        Authentication authentication = new UsernamePasswordAuthenticationToken(updatedUser, updatedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Spring Security session 삭제
     */
    private void expireSession(Long userId) {
        sessionRegistry.getAllPrincipals()
                .stream()
                .filter(p -> p instanceof User principal && principal.getId().equals(userId))
                .forEach(p -> sessionRegistry.getAllSessions(p, false)
                        .forEach(SessionInformation::expireNow)
                );
    }

}
