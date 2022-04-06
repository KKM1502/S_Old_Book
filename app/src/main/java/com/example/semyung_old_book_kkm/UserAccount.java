package com.example.semyung_old_book_kkm;

/**
 * 사용자 계정 모뎀 클래스
 */
public class UserAccount {

    private String IdToken;     //Firebase Uid (고유아이디)
    private String Nickname;    //닉네임
    private String emailId;     //이메일
    private String password;    //비밀번호

    public String getIdToken() {
        return IdToken;
    }

    public void setIdToken(String idToken) {
        IdToken = idToken;
    }

    public UserAccount() { }

    public String getNickname() {return Nickname;  }

    public void setNickname(String nickname) {Nickname = nickname; }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
