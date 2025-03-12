package com.aueb.casino.netsec.demo.service;

import com.aueb.casino.netsec.demo.DTO.UserDto;
import com.aueb.casino.netsec.demo.entity.User;

public interface UserService {
    User findByUsername(String username);
    User save(UserDto userDto);
}
