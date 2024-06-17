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

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/frame")
public class UserFrameController {

	@Autowired
	UserFrameService userFrameService;
		
    // 사용자 개인 프레임 조회
    @GetMapping("/list")
    public List<UserFrameDTO> userFrameList(HttpServletRequest request) {
        // 쿠키에서 userNum과 userId 가져오기
        String userNum = null;
        String userId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userNum")) {
                    userNum = cookie.getValue();
                } else if (cookie.getName().equals("userId")) {
                    userId = cookie.getValue();
                }
            }
        }
        
        if (userNum == null || userId == null) {
            throw new MissingUserInfoException("쿠키에 사용자 정보가 없습니다.");
        } else {
        	// userNum과 userId를 이용해 프레임 리스트 조회
        	return userFrameService.userFrameList(Integer.parseInt(userNum), userId);        	
        }

    }

    // 사용자 개인 프레임 등록
    @PostMapping("/create")
    public String userFrameCreate(@RequestBody UserFrameDTO userFrameDTO, HttpServletRequest request) {
        
    	// 쿠키에서 userNum과 userId 가져오기
        String userNum = null;
        String userId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userNum")) {
                    userNum = cookie.getValue();
                } else if (cookie.getName().equals("userId")) {
                    userId = cookie.getValue();
                }
            }
        }
        
        if (userNum == null || userId == null) {
            throw new MissingUserInfoException("쿠키에 사용자 정보가 없습니다.");
        } else {
        	// userNum과 userId를 이용해 프레임 등록
        	userFrameDTO.setUserNum(Integer.parseInt(userNum));
        	userFrameDTO.setUserId(userId);
        	return userFrameService.userFrameCreate(userFrameDTO);        	
        }

    }

    // 사용자 개인 프레임 삭제
    // delete는 경로에 frameId가 들어가야 하는데, json 형태로 받으려면 Map으로 매핑 시켜서 받아야 한다.
    @DeleteMapping("/delete")
    public String userFrameDelete(@RequestBody Map<String, Long> requestMap, HttpServletRequest request) {
       
    	Long frameId = requestMap.get("frameId");

        // 쿠키에서 userNum과 userId 가져오기
        String userNum = null;
        String userId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userNum")) {
                    userNum = cookie.getValue();
                } else if (cookie.getName().equals("userId")) {
                    userId = cookie.getValue();
                }
            }
        }
        
        if (userNum == null || userId == null) {
            throw new MissingUserInfoException("쿠키에 사용자 정보가 없습니다.");
        } else {
        	// frameId, userNum, userId를 이용해 프레임 삭제
        	return userFrameService.userFrameDelete(frameId, Integer.parseInt(userNum), userId);        	
        }

    }
}
