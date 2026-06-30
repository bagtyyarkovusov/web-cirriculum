package com.bookstore.model;

import java.io.Serializable;

/**
 * Small authenticated-user snapshot safe to store in the HttpSession.
 */
public class SessionUser implements Serializable {

    private final long id;
    private final String username;
    private final String realName;
    private final Role role;

    public SessionUser(long id, String username, String realName, Role role) {
        this.id = id;
        this.username = username;
        this.realName = realName;
        this.role = role;
    }

    public static SessionUser from(User user) {
        return new SessionUser(user.getId(), user.getUsername(), user.getRealName(), user.getRole());
    }

    public long getId() { return id; }

    public String getUsername() { return username; }

    public String getRealName() { return realName; }

    public Role getRole() { return role; }
}
