package com.velikanovdev.recipes.controller;

import com.velikanovdev.recipes.entity.Recipe;
import com.velikanovdev.recipes.entity.User;
import com.velikanovdev.recipes.service.RecipesService;
import com.velikanovdev.recipes.service.UserService;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {
    private final RecipesService recipesService;
    private final UserService userService;

    @Autowired
    public RecipeController(RecipesService recipesService, UserService userService) {
        this.recipesService = recipesService;
        this.userService = userService;
    }

    @PostMapping("/new")
    public ResponseEntity<Map<String, Long>> addRecipe(@AuthenticationPrincipal UserDetails userDetails,
                                                       @RequestBody @Valid Recipe recipe) {
        User user = userService.getUserByEmail(userDetails.getUsername());

        if(user == null) {
            return ResponseEntity.badRequest().build();
        }

        recipe.setAuthor(user); // Set the authenticated user as the author
        recipesService.addRecipe(recipe);
        return ResponseEntity.ok(Map.of("id", recipe.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecipe(@PathVariable long id) {
        Recipe recipe = recipesService.getRecipe(id);
        return  recipe == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(recipe);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails) {
        Recipe recipe = recipesService.getRecipe(id);

        if (recipe == null) {
            return ResponseEntity.notFound().build();
        }

        if (!recipe.getAuthor().getEmail().equals(userDetails.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failed to delete the recipe. Access denied.");
        }

        return recipesService.deleteRecipe(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecipe(@RequestBody @Valid Recipe updatedRecipe, @PathVariable long id,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        Recipe existingRecipe = recipesService.getRecipe(id);

        if (existingRecipe == null) {
            return ResponseEntity.notFound().build();
        }

        if (!existingRecipe.getAuthor().getEmail().equals(userDetails.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failed to update the recipe. Access denied.");
        }

        updatedRecipe.setId(id);

        return recipesService.updateRecipe(updatedRecipe, id);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchRecipes(@RequestParam(value="category", required=false) String category,
                                           @RequestParam(value="name", required=false) String name) {
        if (StringUtils.isNotBlank(category) && StringUtils.isBlank(name)) {
            List<Recipe> recipes = recipesService.searchByCategory(category);
            return recipes.isEmpty() ? ResponseEntity.ok(List.of()) : ResponseEntity.ok(recipes);
        }

        else if (StringUtils.isBlank(category) && StringUtils.isNotBlank(name)) {
            List<Recipe> recipes = recipesService.searchByName(name);
            return recipes.isEmpty() ? ResponseEntity.ok(List.of()) : ResponseEntity.ok(recipes);
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/history")
    public ResponseEntity<?> showAllUserRecipes(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());

        return user == null ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(recipesService.showAllUserRecipes(user));
    }

}
