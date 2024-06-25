package com.example.demo.api;

import java.io.IOException;
import java.util.ArrayList;
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
	ApiService apiService;
	
	@PatchMapping("/MakeUrl")
	public List<String> MakeNewImage(ImageRequestDTO dto) throws IOException, ParseException{
		List<String> Urls = apiService.name(dto);
		return Urls;
	}
	
	@PatchMapping("/MakeTag")
	public String MakeTag(ImageRequestDTO dto) throws IOException {
		String[] tags = apiService.PrintWords(dto).split(",");
		String tag = "";
		for (String tagg : tags) {
			tag += "# " + tagg + "\n";
		}
		return tag;
	}
//	@PatchMapping("/MakeScr")
//	public List<String> MakeImage(ImageRequestDTO dto) throws IOException, ParseException{
//		String tag = "";
//		String[] taag = dto.getTag().split("\n");
//		for (int i=0; i<taag.length-1;i++) {
//			tag += taag[i] + ", ";
//		}
//		tag += taag[taag.length-1];
//		List<String> urls = apiService.TakeURL(tag);
//		return urls;
//	}
	
}
