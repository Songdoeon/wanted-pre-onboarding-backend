package com.wanted.onboarding.auth;

import com.wanted.onboarding.error.CommonErrorCode;
import com.wanted.onboarding.error.exception.NotFoundUserException;
import com.wanted.onboarding.model.User;
import com.wanted.onboarding.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundUserException(CommonErrorCode.NOT_FOUND_USER));

		return new PrincipalDetails(user);
	}
}
