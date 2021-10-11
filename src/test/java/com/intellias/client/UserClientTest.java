package com.intellias.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.intellias.TrialTaskApplication;
import com.intellias.api.Role;
import com.intellias.api.User;
import com.intellias.core.AbstractDbTest;
import feign.Feign;
import feign.Response;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UserClientTest extends AbstractDbTest {

    private final UserClient userClient;

    public UserClientTest() {
        userClient = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new CustomDecoder())
                .target(UserClient.class, "http://localhost:8080/users");
    }

    @BeforeAll
    public static void startServer() throws Exception {
        new TrialTaskApplication().run("server", "config.yml");
    }

    @Test
    public void createTest() {
        User sendUser = new User("Alice");
        sendUser.setId(1);
        sendUser.setRoles(List.of(new Role[]{new Role("admin"), new Role("user")}));
        userClient.create(sendUser);
        User receiveUser = userClient.getById(1);
        assertEquals (receiveUser.getName(), sendUser.getName());
        assertEquals (receiveUser.getId(), sendUser.getId());
        assertEquals (receiveUser.getRoles(), sendUser.getRoles());
    }

    @Test
    public void updateTest() {
        //Creating a new user in case there are none in the database
        userClient.create(new User("Frank"));
        int id = 1;
        User oldUser = userClient.getById(id);
        oldUser.setName("Phil");
        userClient.update(id, oldUser);
        User newUser = userClient.getById(id);
        assertEquals (newUser.getName(), "Phil");
    }

    @Test
    public void deleteTest() {
        //Creating a new user in case there are none in the database
        userClient.create(new User("Frank"));
        int id = 0;
        assert (userClient.getById(id) != null);
        userClient.delete(id);
        assertThrows (NotFoundException.class, () -> userClient.getById(id));
    }

    private static class CustomDecoder implements ErrorDecoder {

        @Override
        public Exception decode(String s, Response response) {
            if (response.status() == 404) {
                return new NotFoundException();
            } else {
                return new Exception();
            }
        }
    }

    private static class NotFoundException extends RuntimeException {    }
}
