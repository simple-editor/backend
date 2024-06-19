package com.example.demo.userframe;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserFrameService {
	
	@Autowired
	UserFrameRepository userFrameRepository;

    // 사용자 개인 프레임 조회
    public List<UserFrameDTO> userFrameList(Long userNum, String userId) {
        List<UserFrameEntity> userFrameEntities = userFrameRepository.findByUserNumAndUserId(userNum, userId);
        return userFrameEntities.stream()
        		.map(UserFrameConverter::entityToDTO)
        		.collect(Collectors.toList());
    }

    // 사용자 개인 프레임 등록
    public String userFrameCreate(UserFrameDTO userFrameDTO) {
        UserFrameEntity userFrameEntity = UserFrameConverter.dtoToEntity(userFrameDTO);
        userFrameRepository.save(userFrameEntity);
        return "프레임 등록 성공";
    }

    // 사용자 개인 프레임 삭제
    public String userFrameDelete(Long frameId, Long userNum, String userId) {
        UserFrameEntity userFrameEntity = userFrameRepository.findByFrameIdAndUserNumAndUserId(frameId, userNum, userId);
        if (userFrameEntity == null) {
            return "해당 프레임을 찾을 수 없습니다.";
        } else {
        	userFrameRepository.delete(userFrameEntity);
        	return "프레임 삭제 성공";        	
        }
    }
}
