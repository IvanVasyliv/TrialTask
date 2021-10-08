package com.intellias.core;

import com.intellias.api.Role;
import com.intellias.api.User;
import java.util.Map;
import java.util.stream.Stream;
import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;

public class UserReducer implements LinkedHashMapRowReducer<Long, User> {

    @Override
    public void accumulate(Map<Long, User> map, RowView rw) {
        User user = map.computeIfAbsent(rw.getColumn("u_id", Long.class),
                id -> rw.getRow(User.class));
        if (rw.getColumn("r_id", Long.class) != null) {
            user.addRole(rw.getRow(Role.class));
        }
    }

}
