package com.example.demo.userlibrary;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.userframe.UserFrameConverter;
import com.example.demo.userframe.UserFrameDTO;
import com.example.demo.userframe.UserFrameEntity;

@Service
public class UserJsonService {
	
	@Autowired
	UserJsonRepository userJsonRepository;

	 public String userJsonCreate(UserJsonDTO jsonDTO) {
	        UserJsonEntity userJsonEntity = UserJsonConverter.DtoTOEntity(jsonDTO);
	        userJsonRepository.save(userJsonEntity);
	        return "라이브러리 등록 성공";
	    }
	 
	 public List<UserJsonDTO> userJsonList(Long userNum){
		 List<UserJsonEntity> userJsonEntities = userJsonRepository.findByUserNum(userNum);
		 return UserJsonConverter.entitiesToDTOs(userJsonEntities);
	 }
	 public List<UserJsonDTO> userJsonList(){
		 List<UserJsonEntity> userJsonEntities = userJsonRepository.findAll();
		 return UserJsonConverter.entitiesToDTOs(userJsonEntities);
	 }
}
