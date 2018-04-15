package app;

public class User {

  private String id;
  private Boolean status;
  private String userName;

  public User(String id, String userName, Boolean status) {
    this.id = id;
    this.status = status;
    this.userName = userName;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  @Override
  public String toString() {
    return "User{" +
        "id='" + id + '\'' +
        ", status=" + status +
        ", userName='" + userName + '\'' +
        '}';
  }
}
