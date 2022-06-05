package com.behl.pehchan.security;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.behl.pehchan.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userId) {
		return convert(userRepository.findById(UUID.fromString(userId))
				.orElseThrow(() -> new UsernameNotFoundException("Bad Credentials")));
	}

	private User convert(com.behl.pehchan.entity.User user) {
		return new User(user.getEmailId(), null, List.of());
	}

}