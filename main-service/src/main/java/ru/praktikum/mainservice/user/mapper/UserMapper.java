package ru.praktikum.mainservice.user.mapper;

import org.springframework.stereotype.Service;
import ru.praktikum.mainservice.user.model.User;
import ru.praktikum.mainservice.user.model.dto.UserDto;

@Service
public class UserMapper {

    public static UserDto userToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    public static User userDtoToUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }
}
