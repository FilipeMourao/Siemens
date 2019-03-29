package siemens.PhotoGallery.helpfulStructures;

public class UserAccount {
    private int id;
    private String userID;
    private String userEmail;
    public UserAccount(){};

    public UserAccount(String userID,String userEmail) {
        this.userID = userID;
        this.userEmail = userEmail;
    }
    public UserAccount(int id,String userID,String userEmail ) {
        this.id = id;
        this.userID = userID;
        this.userEmail = userEmail;
    }
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    //    public int getId() {
//        return id;
//    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
