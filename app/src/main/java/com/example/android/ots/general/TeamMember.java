package com.example.android.ots.general;

/**
 * class TeamMember a user
 */
public class TeamMember {

    private long id;
    private String name;
    private String email;
    private String phone;

    public TeamMember(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

}

