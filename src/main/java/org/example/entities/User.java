package org.example.entities;

public class User {

    private int id;
    private String username;
    private String password;
    private String mail ;
    private String secret;


    public User(int id, String username, String password, String mail, String secret) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.secret = secret;
    }

//    public User(int id, String username, String password) {
//        this(username, password);
//        this.id = id;
//    }

    public User(String username, String password , String mail) {
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.secret = createSecret();
    }

    public User(){

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String createSecret (){
        String secret = "";
        for (int i = 0; i < 10; i++) {
            secret += (char) (Math.random() * 26 + 97);
        }
        return secret;
    }

}
