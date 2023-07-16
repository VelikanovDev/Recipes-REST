package com.velikanovdev.recipes.service;

import com.velikanovdev.recipes.entity.Recipe;
import com.velikanovdev.recipes.entity.User;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface RecipesService {
    void addRecipe(Recipe recipe);
    Recipe getRecipe(long id);
    ResponseEntity<?> deleteRecipe(long id);
    ResponseEntity<?> updateRecipe(Recipe updatedRecipe, long id);
    List<Recipe> searchByCategory(String category);
    List<Recipe> searchByName(String name);
    List<Recipe> showAllUserRecipes(User user);
}
