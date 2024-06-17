package com.example.demo.userlibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.user.UserNotFoundException;
import com.example.demo.userframe.UserFrameDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/Library")
public class UserJsonController {
	
	@Autowired
	UserJsonService userJsonService;
	
	@PostMapping("/create")
    public String userLibCreate(@RequestBody UserJsonDTO userJsonDTO, HttpServletRequest request) {
        
    	// 쿠키에서 userNum과 userId 가져오기
        Long userNum = (long)0;
        String userId = null;
        String userPassword = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userNum")) {
                    userNum = Long.parseLong(cookie.getValue());
                } else if (cookie.getName().equals("userId")) {
                    userId = cookie.getValue();
                }
                else if (cookie.getName().equals("userPassword")) {
                    userPassword = cookie.getValue();
                }
            }
        }

        // userNum과 userId를 이용해 프레임 등록
        userJsonDTO.setUserNum(userNum);
        userJsonDTO.setJsonId(makeJsonId());
        JSONObject jsonObject = JavaToJson(userNum, userJsonDTO);
        if (jsonObject.isEmpty())
        	throw new UserNotFoundException("라이브러리의 정보가 없습니다");
        else {
        userJsonDTO.setJsonFile(jsonObject);
        return userJsonService.userJsonCreate(userJsonDTO);
        }
        }
	public Integer CountLib(JSONObject jsonObject) {
		JSONArray jsonArray = (JSONArray) jsonObject.get("fileNum");
		
		ArrayList<Integer> numlist = new ArrayList<>();
		for (Object arr : jsonArray) {
			JSONObject jobject = (JSONObject) arr;
			numlist.add((Integer)jobject.get("fileNum"));
		}
		Integer numcount = numlist.size();
		return numcount;
	}
	
public JSONObject JavaToJson(Long userNum, UserJsonDTO userJsonDTO) {
	List<UserJsonDTO> userJsonDTOs = userJsonService.userJsonList(userNum);
	Integer numcount = 0;
	
	if (!userJsonDTOs.isEmpty()) {
        numcount = CountLib(userJsonDTO.getUserLib());
    } else {
    	numcount = 0;
//        throw new UserNotFoundException("해당유저의 정보가 없습니다");
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
	case 3:
		filenum = userJsonDTO.getNewFileNum();
		break;
	}
	JSONObject jsonfile = userJsonDTO.getUserLib();	
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
	List<UserJsonDTO> userJson = userJsonService.userJsonList();
	if (!userJson.isEmpty()) {
		jsonId = userJson.get(userJson.size()-1).getJsonId();
	}
	
	jsonId +=1;
	return jsonId;
}

}
