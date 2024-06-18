package com.example.demo.userlibrary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
	 
	 public Integer CountLib(Map<String, Object> map) {
		ArrayList<Object> fileNums = new ArrayList<>();	
		 fileNums.add(map.get("fileNum"));
			
			ArrayList<Integer> numlist = new ArrayList<>();
			if (fileNums.isEmpty()) {
				Integer numcount = numlist.size();
				return numcount;
			}
			else {
			for (Object arr : fileNums) {
				numlist.add((Integer) arr);
			}
			Integer numcount = numlist.size();
			return numcount;
		}
	 }
	public JSONObject JavaToJson(Long userNum, UserJsonDTO userJsonDTO) {
		List<UserJsonDTO> userJsonDTOs = userJsonList(userNum);
		Integer numcount = 0;
		
		if (!userJsonDTOs.isEmpty()) {
	        numcount = CountLib(userJsonDTO.getJsonFile());
	    } else {
	    	numcount = 0;
//	        throw new UserNotFoundException("해당유저의 정보가 없습니다");
	    }	
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
	public Long makeJsonId() {
		Long jsonId = (long)0;
		List<UserJsonDTO> userJson = userJsonList();
		if (!userJson.isEmpty()) {
			jsonId = userJson.get(userJson.size()-1).getJsonId();
		}
		
		jsonId +=(long)1;
		
		return jsonId;
	}
	public String userJsonModify(UserJsonDTO userJsonDTO) {
		UserJsonEntity userJsonEntity = UserJsonConverter.DtoTOEntity(userJsonDTO);
		userJsonRepository.save(userJsonEntity);
		return "라이브러리 수정 성공";
	}
	public JSONObject JavaToJsonModify(Long userNum, UserJsonDTO userJsonDTO) {
		UserJsonEntity userJsonEntity = userJsonRepository.getByUserNum(userNum);
		
	int fileNum = userJsonDTO.getFileNum();
	
	
		JSONArray jsonfile = userJsonDTO.getUserLib();	
		String jsonName = userJsonDTO.getJsonName();
		
		JSONObject makejson = new JSONObject();
		makejson.put("fileNum", fileNum);
		makejson.put("userNum", userNum);
		makejson.put("userLib", jsonfile);
		makejson.put("jsonName", jsonName);
		return makejson;
		
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
