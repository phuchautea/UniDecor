package uni.decor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.decor.entity.Blog;

@Repository
public interface IBlogRepository extends JpaRepository<Blog, Long> {

}