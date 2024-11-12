package z.han.userService.service;

import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import z.han.userService.common.constant.AuthErrorCodes;
import z.han.userService.dto.UserAuthTemplate;
import z.han.userService.dto.UserRequestTemplate;
import z.han.userService.mapper.UserMapper;
import z.han.userService.model.User;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public boolean createUser(UserRequestTemplate userRequestTemplate) {
        User user = userMapper.findUserByUsername(userRequestTemplate.getUsername());
        if (user != null) {
            logger.info("User already exists with username: " + userRequestTemplate.getUsername());
            return false;
        }
        String hashedPassword = passwordEncoder.encode(userRequestTemplate.getPassword());
        User user_for_insertion = User.builder().email(userRequestTemplate.getEmail()).password(
                hashedPassword).timezone(userRequestTemplate.getTimezone()).username(
                userRequestTemplate.getUsername()).build();
        userMapper.insertUser(user_for_insertion);
        return true;
    }

    public int authUser(UserAuthTemplate userAuthTemplate) {
        User user = userMapper.findUserByUsername(userAuthTemplate.getUsername());

        if (user == null) {
            logger.debug("User does not exist with username: " + userAuthTemplate.getUsername());
            return AuthErrorCodes.USER_NOT_FOUND_CODE;
        }

        if (!passwordEncoder.matches(userAuthTemplate.getPassword(), user.getPassword())) {
            return AuthErrorCodes.AUTH_FAILED_CODE;
        }

        return user.getId();
    }


    @Transactional
    public boolean modifyUser(UserRequestTemplate userRequestTemplate) {
        Integer currentId = StpUtil.getLoginIdAsInt();
        User idUser = userMapper.findUserByUserId(currentId);
        idUser.setEmail(userRequestTemplate.getEmail());
        idUser.setPassword(passwordEncoder.encode(userRequestTemplate.getPassword()));
        idUser.setTimezone(userRequestTemplate.getTimezone());
        idUser.setUsername(userRequestTemplate.getUsername());
        userMapper.modifyUser(idUser);
        return true;
    }

    public boolean getUserByUsername(String username) {
        User user = userMapper.findUserByUsername(username);
        if (user == null) {
            logger.info("User does not exist with username: " + username);
            return false;
        }
        return true;
    }

    public boolean getUserByUserId(Integer userId) {
        User user = userMapper.findUserByUserId(userId);
        if (user == null) {
            logger.info("User does not exist with userId: " + userId);
            return false;
        }
        return true;
    }
}
