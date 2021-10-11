package com.intellias.api;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.Length;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

public class Role {
    private long id;
    @NotBlank @Length(min = 2, max = 40)
    private String name;

    public Role() {}

    @JdbiConstructor
    public Role(String name) {
        this.name = name;
    }

    @JsonIgnore
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
    public boolean equals(Object o) {
        if (o.getClass()!=this.getClass())
            return false;
        else {
            return name.equalsIgnoreCase(((Role) o).getName());
        }
    }
}
