package com.example.demo.user;

import java.util.Optional;

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
	public String userSignup(UserDTO userDTO) {
		
		UserEntity userEntity = UserConverter.dtoToEntity(userDTO);
	    
		// 1. ID 중복 체크
	    UserEntity existingUser = userRepository.findByUserId(userDTO.getUserId());
	    	
	    if (existingUser != null) {
	    	return "중복된 아이디 입니다.";
	    } else {	    	
	    	// 2. ID 중복 없음, 사용자 등록 진행
	    	if (userDTO.getUserPassword().equals(userDTO.getUserPasswordConfirm())) {
	    	userRepository.save(userEntity);
	    	return "회원가입이 완료되었습니다.";
	    	}
	    	else
	    		return "비밀번호가 다릅니다.";
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
            userNumCookie.setPath("/");
            userIdCookie.setPath("/");
            
            response.addCookie(userNumCookie);
            response.addCookie(userIdCookie);
            
        }
    }
    
    // 회원 로그아웃
    public String userSignout(String userId, HttpServletResponse response) {
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
            return "로그아웃이 왼료되었습니다.";
        }
        return "Id가 없습니다.(쿠키 인증 실패)";
    }
    
    // 회원 탈퇴
    public String userDelete(Long userNum, String userId, HttpServletResponse response) {
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
    public String findPassword(Long userNum) {
    	Optional<UserEntity> optional = userRepository.findById(userNum);
    	String userPassword = optional.get().getUserPassword();
    	return userPassword;
    }
    public String modifyUser(UserDTO userDTO, Long userNum, String userId, HttpServletResponse response) {
		
		String userNewPassword = userDTO.getUserNewPassword();
		String userNewPasswordConfirm = userDTO.getUserNewPasswordConfirm();
		String usernewQuestion = userDTO.getUserNewQuestion();
		 String usernewAnswer = userDTO.getUserNewAnswer();
    	
		UserEntity entity = userRepository.findByUserId(userId);
		if (!userNewPassword.equals(userNewPasswordConfirm)) {
			return "새로운 비밀번호가 일치하지 않습니다.";
		}
		else {
			entity.setUserPassword(userNewPassword);
			if (!usernewQuestion.isEmpty()&&!usernewAnswer.isEmpty()) {
				entity.setUserQuestion(usernewQuestion);
				entity.setUserAnswer(usernewAnswer);
			}
			entity.setUserNum(userNum);
			entity.setUserId(userId);
			userRepository.save(entity);
			userSignout(userId, response);
			return "회원 수정이 완료되었습니다.\n회원 수정 시 자동 로그아웃 됩니다.\n다시 로그인 해주시길 바랍니다.";
		}
		
	}
	
}
