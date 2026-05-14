package com.barbosa.taskmanager.infrastructure.config.repository;

import com.barbosa.taskmanager.application.dto.UserDetailsDTO;
import com.barbosa.taskmanager.domain.model.entities.Role;
import com.barbosa.taskmanager.domain.model.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(
        properties = {
                "spring.profiles.active=test",
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "spring.sql.init.mode=never"
        }
)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        Role employee = new Role();
        employee.setAuthority("ROLE_EMPLOYEE");
        entityManager.persist(employee);

        Role admin = new Role();
        admin.setAuthority("ROLE_ADMIN");
        entityManager.persist(admin);

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("secret");
        user.addRoles(employee);
        user.addRoles(admin);
        entityManager.persist(user);
        entityManager.flush();
    }

    @Test
    void findByEmail_returnsUserWhenPresent() {
        Optional<User> result = userRepository.findByEmail("test@example.com");
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@example.com");
        assertThat(result.get().getRoles()).hasSize(2);
    }

    @Test
    void findByEmail_returnsEmptyWhenMissing() {
        assertThat(userRepository.findByEmail("missing@example.com")).isEmpty();
    }

    @Test
    void searchUserAndRolesByEmail_returnsOneRowPerRole() {
        List<UserDetailsDTO> rows = userRepository.searchUserAndRolesByEmail("test@example.com");
        assertThat(rows).hasSize(2);
        assertThat(rows).extracting(UserDetailsDTO::username).containsOnly("test@example.com");
        assertThat(rows).extracting(UserDetailsDTO::authority).containsExactlyInAnyOrder("ROLE_EMPLOYEE", "ROLE_ADMIN");
    }

    @Test
    void searchUserAndRolesByEmail_returnsEmptyWhenEmailUnknown() {
        assertThat(userRepository.searchUserAndRolesByEmail("unknown@example.com")).isEmpty();
    }
}
