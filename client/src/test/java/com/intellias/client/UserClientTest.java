package com.intellias.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.intellias.app.TrialTaskApplication;
import com.intellias.core.AbstractDbTest;
import com.intellias.model.Role;
import com.intellias.model.User;
import feign.Feign;
import feign.Response;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import java.nio.file.Path;
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
        new TrialTaskApplication().run("server",
                Path.of(System.getProperty("user.dir")).getParent().toString() + "/config.yml");
    }

    @Test
    public void createTest() {
        User sendUser = new User("Alice");
        sendUser.setId(1L);
        sendUser.setRoles(List.of(new Role[]{new Role("ADMIN"), new Role("USER")}));
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
        User newUser = new User("Frank");
        long id = 2L;
        newUser.setId(id);
        userClient.create(newUser);
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
