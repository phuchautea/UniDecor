package uni.decor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uni.decor.entity.Blog;
import uni.decor.service.BlogService;

import java.util.List;

@Controller
@RequestMapping("/admin/blogs")
public class ManageBlogController {
    @Autowired
    private BlogService blogService;
    @GetMapping
    public String showAllBlogs(Model model) {
        List<Blog> blogs = blogService.getAllBlogs();
        model.addAttribute("blogs", blogs);
        return "admin/blog/list";
    }
    @GetMapping("/add")
    public String addBlogForm(Model model) {
        model.addAttribute("blog", new Blog());
        model.addAttribute("blogs", blogService.getAllBlogs());
        return "admin/blog/add";
    }
    @PostMapping("/add")
    public String addBlog(@ModelAttribute("blog") Blog blog, BindingResult result, Model model) {

        if(result.hasErrors()){
            model.addAttribute("blog", blog);
            return "admin/blog/add";
        }
        blogService.addBlog(blog);
        return "redirect:/admin/blogs";
    }
    @GetMapping("/edit/{id}")
    public String editBlogForm(@PathVariable("id") Long id, Model model) {
        Blog editBlog = blogService.getBlogById(id);
        model.addAttribute("blogs", blogService.getAllBlogs());
        if(editBlog != null) {
            model.addAttribute("blog", editBlog);
            return "admin/blog/edit";
        }else{
            return "not-found";
        }
    }
    @PostMapping("/edit")
    public String editBlog(@ModelAttribute("blog") Blog updatedBlog) {
        blogService.updateBlog(updatedBlog);
        return "redirect:/admin/blogs";
    }

    @GetMapping("/delete/{id}")
    public String deleteBlogForm(@PathVariable("id") Long id, Model model) {
        blogService.deleteBlog(id);
        return "redirect:/admin/blogs";
    }
}
