package com.companion.repositories;

import com.companion.entities.User;
import com.companion.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByStatusNotOrderByNameAsc(UserStatus status);

    Optional<User> findByIdAndStatusNot(Long id, UserStatus status);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByRegistrationNumberIgnoreCase(String registrationNumber);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByRegistrationNumberIgnoreCaseAndIdNot(String registrationNumber, Long id);
}
