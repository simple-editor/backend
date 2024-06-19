package com.example.demo.userframe;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "userframe")
public class UserFrameEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long frameId;
	private Long userNum;
	private String userId;
	private String frameName;
	private int frameWidth;
	private int frameHeight;
}
