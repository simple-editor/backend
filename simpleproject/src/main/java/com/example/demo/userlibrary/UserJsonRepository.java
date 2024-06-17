package com.example.demo.userlibrary;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.userframe.UserFrameEntity;

@Repository
public interface UserJsonRepository extends JpaRepository<UserJsonEntity, Long>{
	List<UserJsonEntity> findByUserNum(Long userNum);
	
	UserJsonEntity getByUserNum(Long userNum);
}
