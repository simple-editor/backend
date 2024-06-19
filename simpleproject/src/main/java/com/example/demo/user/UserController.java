package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService userService;

	//회원 가입
	@PostMapping("/signup")
	public void userSignup(@RequestBody UserDTO userDTO) {
		userService.userSignup(userDTO);
	}
	
	//회원 로그인
	@PostMapping("/signin")
    public String userSignin(@RequestBody UserDTO userDTO, HttpServletResponse response) {
        userService.userSignin(userDTO, response);
        return "로그인 성공";
    }
	
    // 회원 로그아웃
    @PostMapping("/signout")
    public String userSignout(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에서 userNum과 userId 가져오기
        Long userNum = null;
        String userId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userNum")) {
                    userNum = Long.parseLong(cookie.getValue());
                }  if (cookie.getName().equals("userId")) {
                    userId = cookie.getValue();
                }
            }
        }

        if (userNum == null || userId == null) {
            return "로그아웃 실패: 쿠키에 사용자 정보가 없습니다.";
        }

        userService.userSignout(userId, response);
        return "로그아웃 성공";
    }
		
    // 회원 탈퇴
    @DeleteMapping("/delete")
    public String userDelete(@RequestBody UserDTO userDTO, HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에서 userNum과 userId 가져오기
        Long userNum = null;
        String userId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userNum")) {
                    userNum = Long.parseLong(cookie.getValue());
                }  if (cookie.getName().equals("userId")) {
                    userId = cookie.getValue();
                }
            }
        }
        String userPassword = userService.findPassword(userNum);
        if (userNum == null || userId == null) {
            return "쿠키에 사용자 정보가 없습니다.";
        } else {
            // 서비스 계층에 쿠키 값을 전달하고 처리 결과를 반환
        	if (userPassword.equals(userDTO.getUserPasswordConfirm()))
            return userService.userDelete(userNum, userId, response);
        	else
        		return "비밀번호가 일치하지 않습니다.";
        }
    }
    @PostMapping("/modify")
    public String userModify(@RequestBody UserDTO userDTO, HttpServletRequest request, HttpServletResponse response) {
    	Long userNum = null;
        String userId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userNum")) {
                    userNum = Long.parseLong(cookie.getValue());
                }  if (cookie.getName().equals("userId")) {
                    userId = cookie.getValue();
                }
            }
        }
String userPassword = userService.findPassword(userNum);
        if (userNum == null || userId == null) {
            return "쿠키에 사용자 정보가 없습니다.";
        } else {
            if (userDTO.getUserPasswordConfirm().equals(userPassword))
            return userService.modifyUser(userDTO, userNum, userId, response);
            else 
            	return "기존 비밀번호가 일치하지 않습니다.";
        }
    }
}
