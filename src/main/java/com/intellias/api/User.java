package com.intellias.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.Length;

public class User {
    @NotNull
    private Long id;
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
}
