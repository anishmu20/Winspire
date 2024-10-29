package Winspire.service;

import Winspire.dto.UserDto;


import java.util.List;

public interface UserService {

    UserDto create(UserDto userDto);

    List<UserDto> getAllUsers();

    UserDto update(UserDto userDto, String userid);
    UserDto getUser(String userid);
    void delete(String userid);



}
