package com.example.demo.userframe;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFrameRepository extends JpaRepository<UserFrameEntity, Long> {

	List<UserFrameEntity> findByUserNumAndUserId(int userNum, String userId);

	UserFrameEntity findByFrameIdAndUserNumAndUserId(Long frameId, int userNum, String userId);

}
