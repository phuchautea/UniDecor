package uni.decor.service;

import uni.decor.entity.Category;
import uni.decor.repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.decor.utils.SlugUtils;


import java.util.List;
@Service
public class CategoryService {
    @Autowired
    private ICategoryRepository categoryRepository;

    public List<Category> getAllCategories()
    {
        return categoryRepository.findAll();
    }
    public Category getCategoryById(Long id)
    {
        return categoryRepository.findById(id).orElse(null);
    }
    public Category save(Category category)
    {
        return categoryRepository.save(category);
    }

    public void addCategory(Category category)
    {
        String slug = SlugUtils.createSlug(category.getName());
        category.setSlug(slug);
        save(category);
    }
    public void deleteCategory(Long id)
    {
        categoryRepository.deleteById(id);
    }
    public void updateCategory(Category category)
    {
        String slug = SlugUtils.createSlug(category.getName());
        category.setSlug(slug);
        save(category);
    }
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }
}
