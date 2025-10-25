package com.github.vkpro.hw07.controller;

import com.github.vkpro.hw07.annotations.*;
import com.github.vkpro.hw07.dto.UserDto;
import com.github.vkpro.hw07.service.UserService;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Simple CRUD controller for User.
 */
@CustomRestController
@CustomRequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @CustomGetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @CustomGetMapping("/{id}")
    public UserDto getUserById(@CustomPathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @CustomPostMapping("")
    public UserDto createUser(@CustomRequestBody UserDto user) {
        return userService.createUser(user);
    }

    @CustomDeleteMapping("/{id}")
    public void deleteUser(@CustomPathVariable("id") Long id) {
        userService.deleteUser(id);
    }

    @CustomPutMapping("/{id}")
    public UserDto updateUser(@CustomPathVariable("id") Long id, @CustomRequestBody UserDto updatedData) {
        UserDto updated = userService.updateUser(id, updatedData, false);
        return updated != null ? updated : new UserDto(id, "not found", "not found");
    }

    @CustomPatchMapping("/{id}")
    public UserDto patchUser(@CustomPathVariable("id") Long id, @CustomRequestBody UserDto patchData) {
        UserDto updated = userService.updateUser(id, patchData, true);
        return updated != null ? updated : new UserDto(id, "not found", "not found");
    }
}
