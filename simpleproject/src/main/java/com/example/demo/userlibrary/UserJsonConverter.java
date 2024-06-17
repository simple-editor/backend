package com.example.demo.userlibrary;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserJsonConverter {

	public static UserJsonDTO entityToDTO(UserJsonEntity userJsonEntity){
	return UserJsonDTO.builder()
			.jsonId(userJsonEntity.getJsonId())
			.userNum(userJsonEntity.getUserNum())
			.jsonFile(userJsonEntity.getJsonFile())
			.build();
	}
	
	public static UserJsonEntity DtoTOEntity(UserJsonDTO userJsonDTO) {
		return UserJsonEntity.builder()
				.jsonId(userJsonDTO.getJsonId())
				.userNum(userJsonDTO.getUserNum())
				.jsonFile(userJsonDTO.getJsonFile())
				.build();
	}
	public static List<UserJsonDTO> entitiesToDTOs(List<UserJsonEntity> userJsonEntities) {
		return userJsonEntities.stream()
				.map(UserJsonConverter::entityToDTO)
				.collect(Collectors.toList());
		
	}
	
	
}
