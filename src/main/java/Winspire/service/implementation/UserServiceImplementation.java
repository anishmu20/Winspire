package Winspire.service.implementation;

import Winspire.dto.UserDto;
import Winspire.entity.User;
import Winspire.repository.UserRepository;
import Winspire.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    private Logger logger= LoggerFactory.getLogger(UserServiceImplementation.class);

    @Override
    public UserDto create(UserDto userDto) {
        String id= UUID.randomUUID().toString().substring(0,8);
        userDto.setUserId(id);
        User user = mapper.map(userDto, User.class);
        logger.info(userDto.getFullName());
        logger.info(userDto.getEmail());
        user.setVerified(false);
        User savedUser = userRepository.save(user);
        return mapper.map(savedUser,UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> all = userRepository.findAll();
        List<UserDto> listUserdto = all.stream().map((object) -> mapper.map(object, UserDto.class)).toList();
        return listUserdto;
    }

    @Override
    public UserDto update(UserDto userDto, String id) {
        User userById = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userById.setFullName((userDto.getFullName()));
        userById.setEmail(userDto.getEmail());
        userById.setPassword(userDto.getPassword());
        userById.setProfileImage(userDto.getProfileImage());
        userById.setRareCollectionImages(userDto.getRareCollectionImages());
        User UpdatedUser = userRepository.save(userById);
        return mapper.map(UpdatedUser,UserDto.class);
    }

    @Override
    public UserDto getUser(String id) {
        User userById = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return mapper.map(userById,UserDto.class);
    }

    @Override
    public void delete(String id) {
        User userById = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
         userRepository.delete(userById);
    }






}
