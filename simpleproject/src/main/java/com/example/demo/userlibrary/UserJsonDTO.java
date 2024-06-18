package com.example.demo.userlibrary;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserJsonDTO {
	private Long jsonId;
	private Long userNum;
	
	private Map<String, Object> jsonFile = new HashMap<>();
	private String jsonName;
	
	private int fileNum;
	private String userId;
	private String userPassword;
	private String userPasswordConfirm;
	private JSONArray userLib;
	private int newFileNum;
}
