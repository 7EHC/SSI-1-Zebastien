package sit.project.projectv1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sit.project.projectv1.entities.Category;
import sit.project.projectv1.services.CategoryService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategory() {
        return categoryService.getAllCategory();
    }

    @GetMapping("/{categoryId}")
    public Category getCategoryById(@PathVariable Integer categoryId) {
        return categoryService.getCategory(categoryId);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Category create(@RequestBody Category newCategory) {
        return categoryService.addNewCategory(newCategory);
    }
}
