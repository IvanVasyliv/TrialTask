package com.intellias.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.Length;

public class Role {
    @NotNull
    private long id;
    @NotNull
    private long userId;
    @NotBlank @Length(min = 2, max = 40)
    private String name;

    public Role() {}

    public Role(int id, String name) {
        this.id = id;
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
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
