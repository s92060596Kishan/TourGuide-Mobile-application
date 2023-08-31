package com.example.tourguide;
public class User {

    private String email;
    private String fullName;

    private String mobile;
    private String password;
    private String username;


    public User() {

    }

    public User(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }

    public User(String fullName, String email, String mobile,
                String username) {
        this.email = email;
        this.fullName = fullName;
        this.mobile = mobile;
        this.username = username;

    }

/*    public HashMap<String, Object> getAsMap(){
        HashMap<String, Object> userAsMap = new HashMap<>();
        userAsMap.put("username",username);
        userAsMap.put("password",password);
        userAsMap.put("age",age);
        userAsMap.put("name",name);
        //Add or remove more key value pair
        return userAsMap;
    }*/

    public void setpassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public void setPhone(String mobile) {
        this.mobile = mobile;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getpassword() {
        return password;
    }

}
