package com.intellias.api;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.Length;

public class User {
    private long id;
    @NotBlank @Length(min = 2, max = 30)
    private String name;

    public User() {}

    public User(String name) {
        this.name = name;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("User #%d: %s", id, name);
    }
}
