package com.example.demo.userframe;

public class UserFrameConverter {

    public static UserFrameDTO entityToDTO(UserFrameEntity userFrameEntity) {
        return UserFrameDTO.builder()
                .userNum(userFrameEntity.getUserNum())
                .userId(userFrameEntity.getUserId())
                .frameName(userFrameEntity.getFrameName())
                .frameWidth(userFrameEntity.getFrameWidth())
                .frameHeight(userFrameEntity.getFrameHeight())
                .build();
    }

    public static UserFrameEntity dtoToEntity(UserFrameDTO userFrameDTO) {
        return UserFrameEntity.builder()
                .userNum(userFrameDTO.getUserNum())
                .userId(userFrameDTO.getUserId())
                .frameName(userFrameDTO.getFrameName())
                .frameWidth(userFrameDTO.getFrameWidth())
                .frameHeight(userFrameDTO.getFrameHeight())
                .build();
    }
}