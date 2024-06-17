package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.DuplicateUserException;
import com.example.demo.exception.IncorrectPasswordException;
import com.example.demo.exception.UserNotFoundException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	//회원 가입
	public void userSignup(UserDTO userDTO) {
		
		UserEntity userEntity = UserConverter.dtoToEntity(userDTO);
	    
		// 1. ID 중복 체크
	    UserEntity existingUser = userRepository.findByUserId(userDTO.getUserId());
	    	
	    if (existingUser != null) {
	    	throw new DuplicateUserException("중복된 아이디 입니다.");
	    } else {	    	
	    	// 2. ID 중복 없음, 사용자 등록 진행
	    	userRepository.save(userEntity);
	    }

	}
	
    // 회원 로그인
    public void userSignin(UserDTO userDTO, HttpServletResponse response) {
        
    	UserEntity userEntity = userRepository.findByUserId(userDTO.getUserId());
        
        if (userEntity == null) {
            throw new UserNotFoundException("아이디가 없습니다.");
        } else if(!userEntity.getUserPassword().equals(userDTO.getUserPassword())) {
        	throw new IncorrectPasswordException("비밀번호가 틀렸습니다.");
        } else {
            // 로그인 성공, 쿠키 설정
            Cookie userNumCookie = new Cookie("userNum", String.valueOf(userEntity.getUserNum()));
            Cookie userIdCookie = new Cookie("userId", userEntity.getUserId());
            Cookie userPasswordCookie = new Cookie("userPassword", userEntity.getUserPassword());
            userNumCookie.setPath("/");
            userIdCookie.setPath("/");
            userPasswordCookie.setPath("/");
            response.addCookie(userNumCookie);
            response.addCookie(userIdCookie);
            response.addCookie(userPasswordCookie);
        }
    }
    
    // 회원 로그아웃
    public void userSignout(String userId, HttpServletResponse response) {
        if (userId != null) {
            // userNum 쿠키 제거
            Cookie userNumCookie = new Cookie("userNum", null);
            userNumCookie.setPath("/");
            userNumCookie.setMaxAge(0); // 쿠키 만료 시간 0으로 설정하여 삭제

            // userId 쿠키 제거
            Cookie userIdCookie = new Cookie("userId", null);
            userIdCookie.setPath("/");
            userIdCookie.setMaxAge(0); // 쿠키 만료 시간 0으로 설정하여 삭제

            // 쿠키를 응답에 추가하여 클라이언트에 전송
            response.addCookie(userNumCookie);
            response.addCookie(userIdCookie);
        }
    }
    
    // 회원 탈퇴
    public String userDelete(int userNum, String userId, HttpServletResponse response) {
        // DB에서 회원 조회
        UserEntity userEntity = userRepository.findByUserNumAndUserId(userNum, userId);

        if (userEntity == null) {
            return "삭제할 회원이 존재하지 않습니다.";
        } else {
            // 회원 삭제 로직
            userRepository.delete(userEntity);
            
            // 쿠키 삭제 로직
            deleteCookies(response);

            return "회원 삭제 성공";
        }
    }

    // 쿠키 삭제 메서드
    private void deleteCookies(HttpServletResponse response) {
        Cookie userNumCookie = new Cookie("userNum", null);
        Cookie userIdCookie = new Cookie("userId", null);
        userNumCookie.setPath("/");
        userNumCookie.setMaxAge(0);
        userIdCookie.setPath("/");
        userIdCookie.setMaxAge(0);
        response.addCookie(userNumCookie);
        response.addCookie(userIdCookie);
    }
	
}
