package com.example.demo.api;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data

public class ImageRequestDTO {

	private MultipartFile image;
	private String tag;
}
