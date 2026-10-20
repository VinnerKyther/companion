package com.companion.entities;

import com.companion.enums.UserRole;
import com.companion.enums.UserStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_users_registration_number", columnNames = "registration_number")
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Column(name = "registration_number", nullable = false, length = 30)
    private String registrationNumber;

    @Column(nullable = false, length = 120)
    private String course;

    @Column(nullable = false)
    private Integer semester;

    @Column(length = 500)
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected User() {
    }

    public User(String name, String email, String passwordHash, String registrationNumber,
                String course, Integer semester, String bio) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.registrationNumber = registrationNumber;
        this.course = course;
        this.semester = semester;
        this.bio = bio;
        this.status = UserStatus.ACTIVE;
        this.role = UserRole.USER;
    }

    @PrePersist
    private void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    private void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void updateProfile(String name, String email, String registrationNumber,
                              String course, Integer semester, String bio) {
        this.name = name;
        this.email = email;
        this.registrationNumber = registrationNumber;
        this.course = course;
        this.semester = semester;
        this.bio = bio;
    }

    public void anonymize(String anonymousEmail, String anonymousRegistrationNumber,
                          String unusablePasswordHash) {
        name = "Usuário excluído";
        email = anonymousEmail;
        registrationNumber = anonymousRegistrationNumber;
        passwordHash = unusablePasswordHash;
        course = "Não informado";
        semester = 1;
        bio = null;
        status = UserStatus.DELETED;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getCourse() {
        return course;
    }

    public Integer getSemester() {
        return semester;
    }

    public String getBio() {
        return bio;
    }

    public UserStatus getStatus() {
        return status;
    }

    public UserRole getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
