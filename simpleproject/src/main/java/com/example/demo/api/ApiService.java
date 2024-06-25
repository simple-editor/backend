package com.example.demo.api;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.PartMaker;
import com.google.cloud.vertexai.generativeai.ResponseHandler;

@Service
public class ApiService {
	
	private final Storage storage = null;
	
	public List<String> name(ImageRequestDTO dto) throws IOException, ParseException {
		List<String> urls = TakeURL(PrintWords(dto));
		return urls;
	}
	
	public List<String> TakeURL(String reponse) throws IOException, ParseException {
//		String[] words = reponse.split(" ");
//		String word = "";
//		for (int i=0; i<words.length-1; i++) {
//			word += words[i];
//		}
//		word += words[words.length-1];
		String CseKey = "AIzaSyDCnDFGFNG-uIBjaf3GoozcCS741205JfM";
		String CseId = "d5ecbd89615eb40d6";
		List<String> urls = new ArrayList<>();
		Response res = Jsoup.connect(
				String.format("https://www.googleapis.com/customsearch/v1?key=%s&cx=%s&q=%s", CseKey, CseId, reponse))
				.ignoreContentType(true).execute();
		Document doc = res.parse();
		JSONParser par = new JSONParser();
		JSONObject jo_body = (JSONObject) par.parse(doc.selectFirst("body").text());
		JSONArray ja_items = (JSONArray) jo_body.get("items"); // "items":[....]
		if (ja_items==null) {
			return urls;
		} else {
			for (int i = 0; i < ja_items.size(); i++) {
				JSONObject jo_items = (JSONObject) ja_items.get(i);
				JSONObject jo_pgmap = (JSONObject) jo_items.get("pagemap");
				JSONArray ja_imageUrl = (JSONArray) jo_pgmap.get("cse_image");
				if (ja_imageUrl == null) {
					continue;
				}
				JSONObject jo_srcUrl = (JSONObject) ja_imageUrl.get(0);
				// String srcImageUrl = (String) jo_srcUrl.get("src");
				String srcImageUrl = jo_srcUrl.get("src").toString();
				urls.add(srcImageUrl);
			}
			return urls;
		}
	}
	
	@Value("${spring.cloud.gcp.storage.bucket}") // application.yml에 써둔 bucket 이름
    private String bucketName;
	
	@Value("${spring.cloud.gcp.storage.credentials.location}")
	private String keyFileName;
	
	@SuppressWarnings("deprecation")
	public String updateImageBucket(ImageRequestDTO dto) throws IOException {
		InputStream keyFile = ResourceUtils.getURL(keyFileName).openStream();
		Storage storage = StorageOptions.newBuilder()
    			.setCredentials(GoogleCredentials.fromStream(keyFile))
                .build()
                .getService();
				// !!!!!!!!!!!이미지 업로드 관련 부분!!!!!!!!!!!!!!!
        String uuid = UUID.randomUUID().toString(); // Google Cloud Storage에 저장될 파일 이름
        String ext = dto.getImage().getContentType(); // 파일의 형식 ex) JPG

				// Cloud에 이미지 업로드
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, uuid)
                .setContentType(ext).build();

                Blob blob = storage.create(blobInfo, dto.getImage().getInputStream());

//        findMember.updateMemberInfo(dto, uuid); // DB에 반영
        return uuid;
    }
	public String PrintWords(ImageRequestDTO dto) throws IOException {
		VertexAI vertexAI = new VertexAI("vertexai1-425802", "asia-northeast3");	// gemini 호출 객체
				
		String uuid = updateImageBucket(dto);
				GenerativeModel model = new GenerativeModel("gemini-pro-vision", vertexAI);	// gemini 호출 시 사용될 모델 설정, 이미지 사용을 위해 gemini-pro-vision 사용
				// 이미지 없이 텍스트만 사용 시 이전처럼 gemini-pro 사용
				
				GenerateContentResponse response = model.generateContent(	//응답 내용을 받을 변수 response
				ContentMaker.fromMultiModalData(	//입력 데이터에 따른 알맞은 답변 형식 받을 것
						PartMaker.fromMimeTypeAndData("image/png", String.format("gs://client_repuest_image/%s", uuid)),	// 매개변수(전송할 미디어타입 "image/png" : 이미지 형식의 미디어 타입 전송, 이미지 바이트나 이미지 url, 질문할 내용을 담은 텍스트) 
				          "이미지를 설명할 명사 다섯개를 한줄에 쉼표만으로 구분해서 보여줘.띄어쓰기도 없이")
				        );

				    return ResponseHandler.getText(response); // 받아온 응답(텍스트 메세지 한정, getContent 형식도 존재)을 response에 담아 출력 - 답변을 가져올 때 해당 메소드 사
				    
			
			}
}
