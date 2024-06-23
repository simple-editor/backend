package com.example.demo.api;

import java.io.IOException;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ApiController {

	@Autowired
	ApiService cseService;
	
	@PatchMapping("/MakeUrl")
	public List<String> MakeNewImage(ImageRequestDTO dto) throws IOException, ParseException{
		List<String> Urls = cseService.name(dto);
		return Urls;
	}
	
}
