package com.velikanovdev.recipes.service.impl;

import com.velikanovdev.recipes.entity.Recipe;
import com.velikanovdev.recipes.entity.User;
import com.velikanovdev.recipes.repository.RecipeRepository;
import com.velikanovdev.recipes.service.RecipesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RecipesServiceImpl implements RecipesService {
    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipesServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void addRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    @Override
    public Recipe getRecipe(long id) {
        return recipeRepository.findRecipeById(id).orElse(null);
    }

    @Override
    public ResponseEntity<?> deleteRecipe(long id) {
        recipeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> updateRecipe(Recipe updatedRecipe, long id) {
        Recipe recipe = recipeRepository.findRecipeById(id).orElseThrow();

        recipe.setId(id);
        recipe.setName(updatedRecipe.getName());
        recipe.setDescription(updatedRecipe.getDescription());
        recipe.setCategory(updatedRecipe.getCategory());
        recipe.setDate(updatedRecipe.getDate());
        recipe.setIngredients(updatedRecipe.getIngredients());
        recipe.setDirections(updatedRecipe.getDirections());

        recipeRepository.save(recipe);

        return ResponseEntity.noContent().build();
    }

    @Override
    public List<Recipe> searchByCategory(String category) {
        return recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);
    }

    @Override
    public List<Recipe> searchByName(String name) {
        return recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name);
    }

    @Override
    public List<Recipe> showAllUserRecipes(User user) {
        return recipeRepository.findByAuthor(user);
    }

}
