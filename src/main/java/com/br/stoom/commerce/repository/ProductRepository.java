package com.br.stoom.commerce.repository;

import com.br.stoom.commerce.model.Enum.AvailabilityStatus;
import com.br.stoom.commerce.model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<ProductModel, Long> {

    Page<ProductModel> findByAvailability(final AvailabilityStatus availabilityStatus,
                                          final Pageable pageable);

    @Query("SELECT p FROM ProductModel p WHERE LOWER(p.brand) ILIKE LOWER(:brand)")
    Page<ProductModel> findByBrand(@Param("brand") final String brand,
                                   final Pageable pageable);

    @Query("SELECT p FROM ProductModel p WHERE LOWER(p.category) ILIKE LOWER(:category)")
    Page<ProductModel> findByCategory(@Param("category") final String category, final Pageable pageable);

    @Query("SELECT p FROM ProductModel p WHERE LOWER(p.category) ILIKE LOWER(:category) " +
            "AND LOWER(p.brand) ILIKE LOWER(:brand)")
    Page<ProductModel> findByBrandAndCategory(@Param("brand") final String brand,
                                              @Param("category") final String category,
                                              final Pageable pageable);
}
