package com.intellias.client;

import com.intellias.api.Role;
import com.intellias.api.User;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import java.util.List;

public interface RoleClient {
    @RequestLine("GET /{id}")
    Role getById(@Param("id") long id);

    @RequestLine("GET")
    List<Role> getAll();

    @RequestLine("GET ../users/{id}/roles")
    List<Role> getAllFromUser(@Param("id") long id);

    @RequestLine("POST ../users/{id}/roles")
    @Headers("Content-Type: application/json")
    void create(@Param("id") long userId, Role role);

    @RequestLine("PUT /{id}")
    @Headers("Content-Type: application/json")
    Role update(@Param("id") long id, Role role);

    @RequestLine("DELETE /{id}")
    void delete(@Param("id") long id);
}
