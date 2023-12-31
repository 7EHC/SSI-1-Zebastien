package sit.project.projectv1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.project.projectv1.entities.Category;
import sit.project.projectv1.repositories.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    public Category getCategory(Integer categoryID){
        return categoryRepository.findById(categoryID).orElseThrow(
                () ->   new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Id " + categoryID + " does not exist"){
                });
    }

    public Category addNewCategory(Category category){
        return categoryRepository.saveAndFlush(category);
    }
}
