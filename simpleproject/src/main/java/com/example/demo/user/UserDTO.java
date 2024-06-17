package com.example.demo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String userId;
    private String userPassword;
    private String userQuestion;
    private String userAnswer;
    private String userPasswordConfirm;
    private String userNewPassword;
    private String userNewPasswordConfirm;
    private String userNewQuestion;
    private String userNewAnswer;

}