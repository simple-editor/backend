package com.example.demo.user;

//DTO <=> Entity 변환
public class UserConverter {
	 public static UserEntity dtoToEntity(UserDTO userDTO) {
	        return UserEntity.builder()
	                .userId(userDTO.getUserId())
	                .userPassword(userDTO.getUserPassword())
	                .userQuestion(userDTO.getUserQuestion())
	                .userAnswer(userDTO.getUserAnswer())
	                .build();
	    }

	    public static UserDTO entityToDto(UserEntity userEntity) {
	        return UserDTO.builder()
	                .userId(userEntity.getUserId())
	                .userPassword(userEntity.getUserPassword())
	                .userQuestion(userEntity.getUserQuestion())
	                .userAnswer(userEntity.getUserAnswer())
	                .build();
	    }
}
