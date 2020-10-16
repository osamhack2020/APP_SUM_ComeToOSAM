package kr.osam2020.sum.Model;

public class Chatlist implements Comparable<Chatlist> {
    private String id;
    private String username;
    private String timestamp;
    private String imageURL;
    private String isRead;

    public Chatlist() {
    }


    public Chatlist(String timestamp) {
        this.id = "default";
        this.username = "default";
        this.timestamp = timestamp;
        this.imageURL = "default";
        this.isRead = "default";
    }

    public Chatlist(String id, String username, String timestamp, String imageURL, String isRead) {
        this.id = id;
        this.username = username;
        this.timestamp = timestamp;
        this.imageURL = imageURL;
        this.isRead = isRead;
    }


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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public int compareTo(Chatlist chatlist) {
        if (timestamp!=null && chatlist!=null && chatlist.timestamp!=null)
            return this.timestamp.compareTo(chatlist.timestamp) * (-1);
        else
            return -1;
    }
}
