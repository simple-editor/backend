package com.example.demo.userframe;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.MissingUserInfoException;
import com.example.demo.user.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/frame")
public class UserFrameController {

	@Autowired
	UserFrameService userFrameService;
	
	@Autowired
	UserService userService;
		
    // 사용자 개인 프레임 조회
    @GetMapping("/list")
    public List<UserFrameDTO> userFrameList(HttpServletRequest request) {
        // 쿠키에서 userNum과 userId 가져오기
        Long userNum = null;
        String userId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userNum")) {
                    userNum = Long.parseLong(cookie.getValue());
                } else if (cookie.getName().equals("userId")) {
                    userId = cookie.getValue();
                }
            }
        }
        
        if (userNum == null || userId == null) {
            throw new MissingUserInfoException("쿠키에 사용자 정보가 없습니다.");
        } else {
        	// userNum과 userId를 이용해 프레임 리스트 조회
        	return userFrameService.userFrameList(userNum, userId);        	
        }

    }

    // 사용자 개인 프레임 등록
    @PostMapping("/create")
    public String userFrameCreate(@RequestBody UserFrameDTO userFrameDTO, HttpServletRequest request) {
        
    	// 쿠키에서 userNum과 userId 가져오기
        Long userNum = null;
        String userId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userNum")) {
                    userNum = Long.parseLong(cookie.getValue());
                } else if (cookie.getName().equals("userId")) {
                    userId = cookie.getValue();
                }
            }
        }
        
        if (userNum == null || userId == null) {
            throw new MissingUserInfoException("쿠키에 사용자 정보가 없습니다.");
        } else {
        	// userNum과 userId를 이용해 프레임 등록
        	userFrameDTO.setUserNum(userNum);
        	userFrameDTO.setUserId(userId);
        	return userFrameService.userFrameCreate(userFrameDTO);        	
        }

    }

    // 사용자 개인 프레임 삭제
    @DeleteMapping("/delete")
    public String userFrameDelete(@RequestBody UserFrameDTO userFrameDTO, HttpServletRequest request) {
       
    	Long frameId = userFrameDTO.getFrameId();

        // 쿠키에서 userNum과 userId 가져오기
        Long userNum = null;
        String userId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userNum")) {
                    userNum = Long.parseLong(cookie.getValue());
                } else if (cookie.getName().equals("userId")) {
                    userId = cookie.getValue();
                }
            }
        }
        String userPassword = userService.findPassword(userNum);
        
        
        if (userNum == null || userId == null) {
            throw new MissingUserInfoException("쿠키에 사용자 정보가 없습니다.");
        } else {
        	// frameId, userNum, userId를 이용해 프레임 삭제
        	if (userPassword.equals(userFrameDTO.getUserPasswordConfirm()))
        	return userFrameService.userFrameDelete(frameId, userNum, userId);  
        	else
        		return "비밀번호가 일치하지 않습니다.";
        }

    }
}
