package com.intellias.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Locale;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class Role {

    @JsonIgnore
    private transient long id;
    @NotBlank
    @Length(min = 2, max = 40)
    private String name;

    public Role(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o.getClass() != this.getClass()) return false;
        return name.equalsIgnoreCase(((Role) o).getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toUpperCase(Locale.ROOT));
    }
}
