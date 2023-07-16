package com.velikanovdev.recipes.repository;

import com.velikanovdev.recipes.entity.Recipe;
import com.velikanovdev.recipes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findRecipeById(long id);
    List<Recipe> findByCategoryIgnoreCaseOrderByDateDesc(String category);
    List<Recipe> findByNameContainingIgnoreCaseOrderByDateDesc(String name);
    List<Recipe> findByAuthor(User user);
}
