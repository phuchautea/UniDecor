package uni.decor.controller;


import uni.decor.entity.Category;
import uni.decor.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/admin/categories")
public class ManageCategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping
    public String showAllCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin/category/list";
    }
    @GetMapping("/add")
    public String addCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/category/add";
    }
    @PostMapping("/add")
    public String addCategory(@ModelAttribute("category") Category category, BindingResult result, Model model) {
        if(result.hasErrors()){
            model.addAttribute("category", category);
            return "admin/category/add";
        }
        categoryService.addCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable("id") Long id, Model model) {
        Category editCategory = categoryService.getCategoryById(id);
        model.addAttribute("categories", categoryService.getAllCategories());
        if(editCategory != null) {
            model.addAttribute("category", editCategory);
            return "admin/category/edit";
        }else{
            return "not-found";
        }
    }

    @PostMapping("/edit")
    public String editCategory(@ModelAttribute("category") Category updatedCategory) {
        categoryService.updateCategory(updatedCategory);
        return "redirect:/admin/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategoryForm(@PathVariable("id") Long id, Model model) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/categories";
    }
}
