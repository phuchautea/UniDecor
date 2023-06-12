package uni.decor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.decor.entity.Category;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    boolean existsBySlug(String slug);
}
