package com.barbosa.taskmanager.application.dto.service;

import com.barbosa.taskmanager.application.dto.UserDetailsDTO;
import com.barbosa.taskmanager.application.dto.response.UserResponseDTO;
import com.barbosa.taskmanager.domain.model.entities.Role;
import com.barbosa.taskmanager.domain.model.entities.User;
import com.barbosa.taskmanager.infrastructure.config.repository.UserRepository;
import com.barbosa.taskmanager.application.dto.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		List<UserDetailsDTO> result = repository.searchUserAndRolesByEmail(username);
		if (result.isEmpty()) {
			throw new UsernameNotFoundException("Email not found");
		}
		
		User user = new User();
		user.setEmail(result.getFirst().username());
        user.setPassword(result.getFirst().password());
        result.stream()
                .map(dto -> new Role(dto.roleId(), dto.authority()))
                .forEach(user::addRoles);
		
		return user;
	}

    protected User authenticate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Usuário não autenticado");
        }

        Object principal = authentication.getPrincipal();

        String username = switch (principal) {
            case Jwt jwt -> jwt.getClaimAsString("username");
            case UserDetails userDetails -> userDetails.getUsername();
            case String str -> str;
            case null -> throw new IllegalStateException("Tipo de principal não suportado");
            default -> principal.toString();
        };

        return repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getMe() {
        User user = authenticate();
        return UserResponseDTO.fromEntity(user);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(UserResponseDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        return repository.findById(id)
                .map(UserResponseDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }
}
