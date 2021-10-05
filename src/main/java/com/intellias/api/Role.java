package com.intellias.api;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.Length;

public class Role {
    private long id;
    @NotBlank @Length(min = 2, max = 40)
    private String name;

    public Role() {}

    public Role(@Nullable long id, String name) {
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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
