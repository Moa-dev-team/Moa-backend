package com.moa.sercurity.data.userdetail;

import com.moa.domain.user.dao.UserRepository;
import com.moa.domain.user.entity.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User findUser = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("해당 이메일을 가진 유저를 찾을 수 없습니다." + email));

    if (findUser != null) {
      UserDetailsImpl userDetails = new UserDetailsImpl(findUser);
      return userDetails;
    }
    return null;
  }
}
