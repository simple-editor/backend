package com.example.demo.userlibrary;


import java.util.HashMap;
import java.util.Map;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeRegistration;
import org.json.simple.JSONObject;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
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
@Table(name = "userJson")
public class UserJsonEntity {

	@Id
	@Column(name = "json_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long jsonId;
	
	@Column(name = "user_num")
	private Long userNum;
	
	@Type(JsonType.class)
	@Column(columnDefinition = "JSON", name = "json_file")
	private Map<String, Object> jsonFile = new HashMap<>();
}
