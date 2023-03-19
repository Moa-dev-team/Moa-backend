package com.moa.domain.user.service;

import com.moa.domain.user.dao.UserRepository;
import com.moa.domain.user.entity.User;
import com.moa.sercurity.dto.SignupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void registerUser(SignupDto signupDto) {
        User user = User.registerUser(signupDto);
        userRepository.save(user);
    }

}
