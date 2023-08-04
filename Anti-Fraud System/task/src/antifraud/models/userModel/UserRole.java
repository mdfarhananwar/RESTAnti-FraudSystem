package antifraud.models.userModel;

public class UserRole {
    private String username;
    private String role;

    public UserRole() {
    }

    public UserRole(User user) {
        this.username = user.getUsername();
        this.role = user.getRole().getName();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
