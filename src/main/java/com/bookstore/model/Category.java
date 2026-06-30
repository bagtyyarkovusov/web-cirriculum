package com.bookstore.model;

import java.io.Serializable;

public class Category implements Serializable {

    private long id;
    private String name;
    private Long parentId;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
}
