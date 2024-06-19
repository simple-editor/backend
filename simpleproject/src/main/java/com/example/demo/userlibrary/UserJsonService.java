package com.example.demo.userlibrary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	 
	public JSONObject JavaToJson(Long userNum, UserJsonDTO userJsonDTO) {
		List<UserJsonDTO> userJsonDTOs = userJsonList(userNum);
		Integer numcount = userJsonDTOs.size();	
		Integer filenum = 0;
		switch(numcount) {
		case 0:
			filenum = 1;
			break;
		case 1 :
			filenum = 2;
			break;
		case 2:
			filenum = 3;
			break;
		}
		JSONArray jsonfile = userJsonDTO.getUserLib();	
		String jsonName = userJsonDTO.getJsonName();
		
		
		JSONObject makejson = new JSONObject();
		makejson.put("fileNum", filenum);
		makejson.put("userNum", userNum);
		makejson.put("userLib", jsonfile);
		makejson.put("jsonName", jsonName);
		
		return makejson;
		}
	
	public String userJsonModify(UserJsonDTO userJsonDTO) {
		UserJsonEntity userJsonEntity = UserJsonConverter.DtoTOEntity(userJsonDTO);
		userJsonRepository.save(userJsonEntity);
		return "라이브러리 수정 성공";
	}
	public Map<String, Object> JavaToJsonModify(Long jsonId, UserJsonDTO userJsonDTO) {
		Optional<UserJsonEntity> optional = userJsonRepository.findById(jsonId);
		Map<String, Object> makejson = optional.get().getJsonFile();
	
		JSONArray jsonfile = userJsonDTO.getUserLib();	
		String jsonName = userJsonDTO.getJsonName();
		
		makejson.put("userLib", jsonfile);
		makejson.put("jsonName", jsonName);
		return makejson;
		
	}
	
	public Long findJsonId(UserJsonDTO userJsonDTO, Long userNum) {
		List<UserJsonDTO> entities = userJsonList(userNum);
		UserJsonDTO dto = new UserJsonDTO();
		for (UserJsonDTO dto2 : entities) {
			Map<String, Object> json = dto2.getJsonFile();
			if ((int)json.get("fileNum") == userJsonDTO.getFileNum())
				dto = dto2;
		}
		Long jsonId = dto.getJsonId();
		return jsonId;
	}
	public List<UserJsonEntity> LookLib(Long userNum) {
		return userJsonRepository.findByUserNum(userNum);
	}
	
	public String userJsonDelete(Long jsonId) {
		Optional<UserJsonEntity> userJsonEntity = userJsonRepository.findById(jsonId);
		if (userJsonEntity.isEmpty()) {
			return "해당라이브러리를 찾을 수 없습니다.";
		}
		else {
		userJsonRepository.deleteById(jsonId);
		return "라이브러리 삭제 성공";
		}
	}
}
