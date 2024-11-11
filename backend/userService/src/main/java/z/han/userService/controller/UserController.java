package z.han.userService.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import z.han.userService.dto.UserAuthTemplate;
import z.han.userService.dto.UserRequestTemplate;
import z.han.userService.service.UserService;

import static z.han.common.constant.AuthErrorCodes.AUTH_FAILED_CODE;
import static z.han.common.constant.AuthErrorCodes.USER_NOT_FOUND_CODE;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @RequestMapping("/user/register")
    public SaResult createUser(@Valid @RequestBody UserRequestTemplate userRequestTemplate) {
        boolean success = userService.createUser(userRequestTemplate);
        if (!success) {
            return SaResult.error("User already exists");
        }
        return SaResult.code(HttpStatus.CREATED.value()).setMsg("User registered successfully");
    }

    //    @RequestMapping("/user/authenticate")
//    public SaResult userAuthenticate(@Valid @RequestBody UserRequestTemplate userRequestTemplate) {
//        boolean result = userService.authUser(userRequestTemplate);
//        if (!result) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
//        }
//        return ResponseEntity.status(HttpStatus.OK).body("Successfully authenticated");
//    }
    @SaCheckLogin
    @RequestMapping("/user/modify")
    public SaResult modifyUser(@Valid @RequestBody UserRequestTemplate userRequestTemplate) {
        boolean result = userService.modifyUser(userRequestTemplate);
        if (!result) {
            return SaResult.code(HttpStatus.UNAUTHORIZED.value()).setMsg("User not authorized");
        }
        return SaResult.ok("User modified successfully");
    }

    @RequestMapping("/user/login")
    public SaResult login(@Valid @RequestBody UserAuthTemplate userAuthTemplate) {
        int result = userService.authUser(userAuthTemplate);
        if (result != USER_NOT_FOUND_CODE && result != AUTH_FAILED_CODE) {
            StpUtil.login(result);
            return SaResult.ok("User logged in successfully");
        } else if (result == USER_NOT_FOUND_CODE) {
            return SaResult.error("User not found");
        } else {
            return SaResult.error("User not authorized");
        }
    }

    @RequestMapping("/user/isLogin")
    public SaResult isLogin() {
        return SaResult.ok("Login status:" + StpUtil.isLogin());
    }

    @RequestMapping("/user/tokenInfo")
    public SaResult tokenInfo() {
        return SaResult.data(StpUtil.getTokenInfo());
    }

    @RequestMapping("/user/logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }
}
