package siemens.PhotoGallery.helpfulStructures;

public class UserAccount {
    private int id;
    private String userID;
    private String userEmail;
    private int numberOfImages;
    public UserAccount(String userID,String userEmail) { // normal constructor
        this.userID = userID;
        this.userEmail = userEmail;
    }

public UserAccount(int id,String userID,String userEmail, int numberOfImages ) {// contructor to build the photo from the database elements
    this.id = id;
    this.userID = userID;
    this.userEmail = userEmail;
    this.numberOfImages = numberOfImages;
}
    public String getUserID() {
        return userID;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getNumberOfImages() {
        return numberOfImages;
    }

    public void setNumberOfImages(int numberOfImages) {
        this.numberOfImages = numberOfImages;
    }
}
