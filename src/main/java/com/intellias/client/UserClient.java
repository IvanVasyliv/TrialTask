package com.intellias.client;

import com.intellias.api.User;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import java.util.List;

public interface UserClient {
    @RequestLine("GET /{id}")
    User getById(@Param("id") long id);

    @RequestLine("GET")
    List<User> getAll();

    @RequestLine("POST")
    @Headers("Content-Type: application/json")
    void create(User user);

    @RequestLine("PUT /{id}")
    @Headers("Content-Type: application/json")
    User update(@Param("id") long id, User user);

    @RequestLine("DELETE /{id}")
    void delete(@Param("id") long id);
}
