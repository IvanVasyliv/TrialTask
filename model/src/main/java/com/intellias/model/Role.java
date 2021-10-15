package com.intellias.model;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class Role {
    private transient long id;
    @NotBlank
    @Length(min = 2, max = 40)
    private String name;

    public Role(String name) {
        this.name = name;
    }
}
