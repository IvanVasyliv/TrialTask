package com.intellias.model;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class User {

    private long id;
    @NotNull
    @NotBlank
    @Length(min = 2, max = 30)
    private String name;
    private List<Role> roles = new ArrayList<>();

    public User(String name) {
        this.name = name;
    }
}
