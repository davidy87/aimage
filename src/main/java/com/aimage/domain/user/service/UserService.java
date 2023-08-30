package com.aimage.domain.user.service;

import com.aimage.domain.image.dto.ImageVO;
import com.aimage.domain.image.entity.Image;
import com.aimage.domain.image.repository.ImageRepository;
import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.repository.UserRepository;
import com.aimage.domain.user.dto.UserVO;
import com.aimage.web.exception.AimageException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static com.aimage.constant.PageConst.PAGE_SIZE;
import static com.aimage.domain.user.dto.UserDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final ImageRepository imageRepository;

    private final PasswordEncoder passwordEncoder;

    public UserVO join(Signup signupDto) {
        // 비밀번호 재입력 일치 확인
        if (!signupDto.getPassword().equals(signupDto.getConfirmPassword())) {
            throw new AimageException("confirmPassword", "비밀번호를 다시 확인해주세요.");
        }

        User user = User.builder()
                .username(signupDto.getUsername())
                .email(signupDto.getEmail())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .build();

        userRepository.save(user);

        return new UserVO(user.getId(), user.getUsername(), user.getEmail());
    }

    public UserVO login(String email, String password) {
        log.info("--- In UserService (login) ---");

        User loginUser = userRepository.findByEmail(email)
                .filter(user -> user.getPassword().equals(password))
                .orElseThrow(() -> new AimageException("loginError", "이메일 또는 비밀번호를 잘못 입력했습니다."));

        return new UserVO(loginUser.getId(), loginUser.getUsername(), loginUser.getEmail());
    }

    public UserVO findUserToResetPw(String email) {
        User userFound = userRepository.findByEmail(email)
                .orElseThrow(() -> new AimageException("pwInquiry", "계정을 찾을 수 없습니다."));

        log.info("User found = {}", userFound);
        return new UserVO(userFound.getId(), userFound.getUsername(), userFound.getEmail());
    }

    public UserVO updateUsername(Long id, UpdateUsername updateUsername) {
        String newUsername = updateUsername.getUsername();
        User userToUpdate = userRepository.findById(id)
                .filter(user -> !user.getUsername().equals(newUsername))
                .orElseThrow(() -> new AimageException("usernameUpdate", "닉네임이 이전과 같습니다."));

        userToUpdate.updateUsername(newUsername);

        // 새로운 인증 생성 및 추가
        updateAuth(userToUpdate);

        return new UserVO(userToUpdate.getId(), userToUpdate.getUsername(), userToUpdate.getEmail());
    }

    public UserVO updatePassword(Long userId, UpdatePassword updatePassword) {
        String newPassword = updatePassword.getPassword();
        String confirmPassword = updatePassword.getConfirmPassword();

        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new AimageException("pwInquiry", "계정을 찾을 수 없습니다."));

        if (!newPassword.equals(confirmPassword)) {
            throw new AimageException("confirmPassword", "비밀번호를 다시 확인해주세요.");
        }

        userToUpdate.updatePassword(newPassword);

        // 새로운 인증 생성 및 추가
        updateAuth(userToUpdate);

        return new UserVO(userToUpdate.getId(), userToUpdate.getUsername(), userToUpdate.getEmail());
    }

    public void deleteAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AimageException("이미 존재하지 않는 사용자 입니다."));

        userRepository.delete(user);
    }

    public Page<ImageVO> findSavedImages(Long userId, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, PAGE_SIZE, Sort.Direction.DESC, "id");

        return imageRepository.findAllByOwnerId(userId, pageable)
                .map(image ->
                        new ImageVO(image.getId(), image.getPrompt(), image.getUrl(), image.getOwner().getUsername()));
    }

    public ImageVO findByOwnerIdAndImageId(Long userId, Long imageId) {
        Image image = imageRepository.findByOwnerIdAndId(userId, imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        return new ImageVO(image.getId(), image.getPrompt(), image.getUrl(), image.getOwner().getUsername());
    }

    /**
     * 새로운 인증 생성 및 추가
     */
    private void updateAuth(User updatedUser)  {
        Authentication authentication = new UsernamePasswordAuthenticationToken(updatedUser, updatedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
