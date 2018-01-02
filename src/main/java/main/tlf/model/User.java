package main.tlf.model;

public class User {
    private boolean admin;
    private String type;

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User(boolean admin, String type) {
        this.admin = admin;
        this.type = type;
    }
}
