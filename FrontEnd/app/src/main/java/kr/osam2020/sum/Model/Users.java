package kr.osam2020.sum.Model;

public class Users {
    private String id;
    private String username;
    private String introduction;
    private String imageURL;

    // Constructors
    public Users() {

    }

    public Users(String id, String username, String introduction, String imageURL) {
        this.id = id;
        this.username = username;
        this.introduction = introduction;
        this.imageURL = imageURL;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
