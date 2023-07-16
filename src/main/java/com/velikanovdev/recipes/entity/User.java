package com.velikanovdev.recipes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Pattern(regexp = ".+@.+\\..+", message = "Email is not valid!")
    @NotBlank
    private String email;

    @Size(min = 8, message = "Password must contain at least 8 characters")
    @Pattern(regexp = "^.*(?=.{8,}).*$", message = "Password is not valid")
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Recipe> recipes;

}
