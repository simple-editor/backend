package com.example.demo.userframe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFrameDTO {
	private int userNum;
	private String userId;
	private String frameName;
	private int frameWidth;
	private int frameHeight;
}
