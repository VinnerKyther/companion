package com.companion.services;

import com.companion.dtos.user.UserRequestDTO;
import com.companion.dtos.user.UserResponseDTO;
import com.companion.dtos.user.UserUpdateRequestDTO;
import com.companion.entities.User;
import com.companion.enums.UserStatus;
import com.companion.exceptions.DuplicateResourceException;
import com.companion.exceptions.ResourceNotFoundException;
import com.companion.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDTO create(UserRequestDTO request) {
        String email = normalizeEmail(request.email());
        String registrationNumber = normalizeRegistrationNumber(request.registrationNumber());
        validateUniqueData(email, registrationNumber);

        User user = new User(
                request.name().trim(),
                email,
                passwordEncoder.encode(request.password()),
                registrationNumber,
                request.course().trim(),
                request.semester(),
                normalizeOptionalText(request.bio())
        );

        return toResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return userRepository.findAllByStatusNotOrderByNameAsc(UserStatus.DELETED)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        return toResponse(findActiveEntityById(id));
    }

    @Transactional
    public UserResponseDTO update(Long id, UserUpdateRequestDTO request) {
        User user = findActiveEntityById(id);
        String email = normalizeEmail(request.email());
        String registrationNumber = normalizeRegistrationNumber(request.registrationNumber());

        validateUniqueDataForUpdate(email, registrationNumber, id);
        user.updateProfile(
                request.name().trim(),
                email,
                registrationNumber,
                request.course().trim(),
                request.semester(),
                normalizeOptionalText(request.bio())
        );

        return toResponse(userRepository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        User user = findActiveEntityById(id);
        String randomValue = UUID.randomUUID().toString();

        user.anonymize(
                "deleted-" + id + "@companion.invalid",
                "DELETED-" + id,
                passwordEncoder.encode(randomValue)
        );
        userRepository.save(user);
    }

    private User findActiveEntityById(Long id) {
        return userRepository.findByIdAndStatusNot(id, UserStatus.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID " + id));
    }

    private void validateUniqueData(String email, String registrationNumber) {
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateResourceException("Já existe um usuário cadastrado com esse e-mail");
        }
        if (userRepository.existsByRegistrationNumberIgnoreCase(registrationNumber)) {
            throw new DuplicateResourceException("Já existe um usuário cadastrado com essa matrícula");
        }
    }

    private void validateUniqueDataForUpdate(String email, String registrationNumber, Long id) {
        if (userRepository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new DuplicateResourceException("Já existe outro usuário cadastrado com esse e-mail");
        }
        if (userRepository.existsByRegistrationNumberIgnoreCaseAndIdNot(registrationNumber, id)) {
            throw new DuplicateResourceException("Já existe outro usuário cadastrado com essa matrícula");
        }
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizeRegistrationNumber(String registrationNumber) {
        return registrationNumber.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizeOptionalText(String text) {
        return text == null || text.isBlank() ? null : text.trim();
    }

    private UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRegistrationNumber(),
                user.getCourse(),
                user.getSemester(),
                user.getBio(),
                user.getStatus(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
