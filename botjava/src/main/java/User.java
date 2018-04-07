public class User {
  private Long id;
  private Boolean status;

  public User(Long id, Boolean status) {
    this.id = id;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", status=" + status +
        '}';
  }
}
