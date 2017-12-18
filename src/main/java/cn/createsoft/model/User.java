package cn.createsoft.model;

public class User {

   private int userId;

   private String phoneNum;
   private String password;

   private String token;

   public String getToken() {
      return token;
   }

   public void setToken(String token) {
      this.token = token;
   }

   public String getPhoneNum() {
      return phoneNum;
   }

   public void setPhoneNum(String phoneNum) {
      this.phoneNum = phoneNum;
   }



   public int getUserId() {
      return userId;
   }

   public void setUserId(int userId) {
      this.userId = userId;
   }


   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }
}
