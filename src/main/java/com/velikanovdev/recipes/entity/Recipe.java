package com.velikanovdev.recipes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recipes")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Recipe {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "description")
    private String description;

    @NotBlank
    @Column(name = "category")
    private String category;

    @UpdateTimestamp
    @Column(name = "date")
    private LocalDateTime date;

    @NotEmpty
    @CollectionTable(name = "ingredients", joinColumns = @JoinColumn(name = "id"))
    @ElementCollection
    @Column
    private List<String> ingredients;

    @NotEmpty
    @CollectionTable(name = "directions", joinColumns = @JoinColumn(name = "id"))
    @ElementCollection
    @Column(name = "directions")
    private List<String> directions;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

}
