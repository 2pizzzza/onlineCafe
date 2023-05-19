package com.cb.repository;

import com.cb.model.Cafe;
import com.cb.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    @Query("SELECT DISTINCT m.category FROM MenuItem m")
    List<String> findDistinctCategories();

}
