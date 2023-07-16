package com.velikanovdev.recipes.controller;

import com.velikanovdev.recipes.entity.Recipe;
import com.velikanovdev.recipes.entity.User;
import com.velikanovdev.recipes.service.RecipesService;
import com.velikanovdev.recipes.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeControllerTest {
    @Mock
    private RecipesService recipesService;

    @Mock
    private UserService userService;

    @InjectMocks
    private RecipeController recipeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddRecipe() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        User user = new User();
        user.setEmail("user@example.com");

        when(userDetails.getUsername()).thenReturn(user.getEmail());
        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);

        Recipe recipe = new Recipe();
        recipe.setAuthor(user);

        // Mocking the void method behavior
        doNothing().when(recipesService).addRecipe(recipe);

        // Act
        ResponseEntity<Map<String, Long>> response = recipeController.addRecipe(userDetails, recipe);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.singletonMap("id", recipe.getId()), response.getBody());

        verify(recipesService, times(1)).addRecipe(recipe);
    }

    @Test
    void testAddRecipe_InvalidUser() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        User user = new User();
        user.setEmail("user@example.com");

        when(userDetails.getUsername()).thenReturn(user.getEmail());
        when(userService.getUserByEmail(user.getEmail())).thenReturn(null);

        Recipe recipe = new Recipe();

        // Act
        ResponseEntity<Map<String, Long>> response = recipeController.addRecipe(userDetails, recipe);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());

        verify(recipesService, never()).addRecipe(any(Recipe.class));
    }


    @Test
    void testGetRecipe_NotFound() {
        // Arrange
        long recipeId = 1L;

        when(recipesService.getRecipe(recipeId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = recipeController.getRecipe(recipeId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(recipesService, times(1)).getRecipe(recipeId);
    }

    @Test
    void testSearchRecipes_ByCategory() {
        // Arrange
        String category = "desserts";
        List<Recipe> recipes = Collections.singletonList(new Recipe());

        when(recipesService.searchByCategory(category)).thenReturn(recipes);

        // Act
        ResponseEntity<?> response = recipeController.searchRecipes(category, "");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(recipes, response.getBody());

        verify(recipesService, times(1)).searchByCategory(category);
    }

    @Test
    void testSearchRecipes_ByName() {
        // Arrange
        String name = "chocolate";
        List<Recipe> recipes = Collections.singletonList(new Recipe());

        when(recipesService.searchByName(name)).thenReturn(recipes);

        // Act
        ResponseEntity<?> response = recipeController.searchRecipes(null, name);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(recipes, response.getBody());

        verify(recipesService, times(1)).searchByName(name);
    }

}
