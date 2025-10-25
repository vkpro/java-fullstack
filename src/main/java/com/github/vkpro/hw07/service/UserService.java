package com.github.vkpro.hw07.service;

import com.github.vkpro.hw07.dto.UserDto;
import com.github.vkpro.hw07.model.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Business logic layer.
 */
public class UserService {
    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<UserDto> getAllUsers() {
        return users.values().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        var user = users.get(id);
        return user != null ? toDto(user) : null;
    }

    public UserDto createUser(UserDto dto) {
        var user = new User(idGenerator.getAndIncrement(), dto.getName(), dto.getEmail());
        users.put(user.getId(), user);
        return toDto(user);
    }

    public UserDto updateUser(Long id, UserDto updatedData, boolean partial) {
        var existing = users.get(id);
        if (existing == null) return null;

        // For PATCH (partial = true), only update non-null fields
        if (partial) {
            if (updatedData.getName() != null) existing.setName(updatedData.getName());
            if (updatedData.getEmail() != null) existing.setEmail(updatedData.getEmail());
        } else {
            // For PUT (partial = false), replace all fields
            existing.setName(updatedData.getName());
            existing.setEmail(updatedData.getEmail());
        }

        return toDto(existing);
    }


    public void deleteUser(Long id) {
        users.remove(id);
    }

    // ===== Utility mapping =====
    private UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}
