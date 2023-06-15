package uni.decor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.decor.entity.Blog;
import uni.decor.repository.IBlogRepository;

import java.util.List;

@Service
public class BlogService {
    @Autowired
    private IBlogRepository blogRepository;

    public List<Blog> getAllBlogs()
    {
        return blogRepository.findAll();
    }
    public Blog getBlogById(Long id)
    {
        return blogRepository.findById(id).orElse(null);
    }
    public Blog save(Blog blog)
    {
        return blogRepository.save(blog);
    }

    public void addBlog(Blog blog)
    {
        save(blog);
    }
    public void deleteBlog(Long id)
    {
        blogRepository.deleteById(id);
    }
    public void updateBlog(Blog blog)
    {
        save(blog);
    }
}

