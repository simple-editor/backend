package com.example.demo.userlibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.*;
import com.example.demo.user.*;
import com.example.demo.userframe.UserFrameDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/Library")
public class UserJsonController {
	
	@Autowired
	UserJsonService userJsonService;
	@Autowired
	UserService userService;
	
	@PostMapping("/create")
    public String userLibCreate(@RequestBody UserJsonDTO userJsonDTO, HttpServletRequest request) {
        
    	// 쿠키에서 userNum과 userId 가져오기
        Long userNum = (long)0;
        String userId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userNum")) {
                    userNum = Long.parseLong(cookie.getValue());
                } 
                if (cookie.getName().equals("userId")) {
                    userId = cookie.getValue();
                }
             
            }
        }
        userJsonDTO.setUserNum(userNum);
        JSONObject jsonObject = userJsonService.JavaToJson(userNum, userJsonDTO);
        if (jsonObject.isEmpty())
        	throw new UserNotFoundException("라이브러리의 정보가 없습니다");
        else {
        userJsonDTO.setJsonFile(jsonObject);
        return userJsonService.userJsonCreate(userJsonDTO);
        }
        }

	@PostMapping("/modify")
	public String userLibmodify(@RequestBody UserJsonDTO userJsonDTO, HttpServletRequest request) {
		Long userNum = (long)0;
        String userId = null;
        
       String userPassword = userService.findPassword(userNum);
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userNum")) {
                    userNum = Long.parseLong(cookie.getValue());
                } 
                if (cookie.getName().equals("userId")) {
                    userId = cookie.getValue();
                }
                
            }
        }
        if (!userPassword.equals(userJsonDTO.getUserPasswordConfirm()))
		return "비밀번호가 일치하지 않습니다";
        else {
        	userJsonDTO.setUserNum(userNum);
            Long jsonId = userJsonService.findJsonId(userJsonDTO, userNum);
        	userJsonDTO.setJsonId(jsonId);
            Map<String, Object> jsonObject = userJsonService.JavaToJsonModify(jsonId, userJsonDTO);
            userJsonDTO.setJsonFile(jsonObject);
		return userJsonService.userJsonModify(userJsonDTO);
        }
	}
	
@PostMapping("/look")
public List<UserJsonDTO> userLibLook(HttpServletRequest request) {
	Long userNum = (long)0;
	Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("userNum")) {
                userNum = Long.parseLong(cookie.getValue());
            }
        }
    }
    return userJsonService.userJsonList(userNum);
}
	
	
	@DeleteMapping("/delete")
	public String userLibDelete(@RequestBody UserJsonDTO userJsonDTO, HttpServletRequest request) {
		Long jsonId = userJsonDTO.getJsonId();
		String userPasswordConfirm = userJsonDTO.getUserPasswordConfirm();

        Long userNum = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userNum")) {
                    userNum = Long.parseLong(cookie.getValue());
                } 
                
            }
        }
        String userPassword = userService.findPassword(userNum);
        
        if (userNum == null || jsonId == null) {
            throw new MissingUserInfoException("쿠키에 사용자 정보가 없습니다.");
        } else {
        	if (userPassword.equals(userPasswordConfirm))
        	return userJsonService.userJsonDelete(jsonId);
        	else
        		return "비밀번호가 일치하지 않습니다.";
        }
	}
}
