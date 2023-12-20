package com.serhiihurin.shop.online_shop.dao;

import com.serhiihurin.shop.online_shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) DESC")
    List<Product> findAllOrderByAverageRatingDesc();

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) ASC")
    List<Product> findAllOrderByAverageRatingAsc();

    @Query("SELECT p FROM Product p "+
            "ORDER BY p.price DESC")
    List<Product> findAllOrderByPriceDesc();

    @Query("SELECT p FROM Product p "+
            "ORDER BY p.price ASC")
    List<Product> findAllOrderByPriceAsc();

    Optional<Product> getProductByName(String name);

    List<Product> getProductsByShopId(Long id);

    @Query("SELECT AVG(CAST(f.rate AS int)) FROM Feedback f WHERE f.product.id = :productId")
    Double calculateAverageRatingForProduct(@Param("productId") Long productId);

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE p.shop.id = :id " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) DESC")
    List<Product> getProductsByShopIdOrderByAverageRatingDesc(@Param("id") Long id);

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE p.shop.id = :id " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) ASC")
    List<Product> getProductsByShopIdOrderByAverageRatingAsc(@Param("id") Long id);

    List<Product> getProductsByShopIdOrderByPriceDesc(Long id);

    List<Product> getProductsByShopIdOrderByPriceAsc(Long id);

    List<Product> getProductsByNameContains(String productName);

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) DESC")
    List<Product> getProductsByNameContainsOrderByAverageRatingDesc(@Param("productName") String productName);

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) ASC")
    List<Product> getProductsByNameContainsOrderByAverageRatingAsc(@Param("productName") String productName);

    List<Product> getProductsByNameContainsOrderByPriceDesc(String productName);

    List<Product> getProductsByNameContainsOrderByPriceAsc(String productName);

    List<Product> getProductsByNameContainsAndPriceBetween(
            String productName,
            Double minimalPrice,
            Double maximalPrice
    );

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) " +
            "AND p.price BETWEEN :minimalPrice AND :maximalPrice " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) DESC")
    List<Product> getProductsByNameContainsAndPriceBetweenOrderByAverageRatingDesc(
            @Param("productName") String productName,
            @Param("minimalPrice") Double minimalPrice,
            @Param("maximalPrice") Double maximalPrice
    );

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) " +
            "AND p.price BETWEEN :minimalPrice AND :maximalPrice " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) ASC")
    List<Product> getProductsByNameContainsAndPriceBetweenOrderByAverageRatingAsc(
            @Param("productName") String productName,
            @Param("minimalPrice") Double minimalPrice,
            @Param("maximalPrice") Double maximalPrice
    );

    List<Product> getProductsByNameContainsAndPriceBetweenOrderByPriceDesc(
            String productName,
            Double minimalPrice,
            Double maximalPrice
    );

    List<Product> getProductsByNameContainsAndPriceBetweenOrderByPriceAsc(
            String productName,
            Double minimalPrice,
            Double maximalPrice
    );

    List<Product> getProductsByNameContainsAndPriceGreaterThan(
            String productName,
            Double price
    );

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) " +
            "AND p.price > :price " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) DESC")
    List<Product> getProductsByNameContainsAndPriceGreaterThanOrderByAverageRatingDesc(
            @Param("productName") String productName,
            @Param("price") Double price
    );

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) " +
            "AND p.price > :price " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) ASC")
    List<Product> getProductsByNameContainsAndPriceGreaterThanOrderByAverageRatingAsc(
            @Param("productName") String productName,
            @Param("price") Double price
    );

    List<Product> getProductsByNameContainsAndPriceGreaterThanOrderByPriceDesc(
            String productName,
            Double price
    );

    List<Product> getProductsByNameContainsAndPriceGreaterThanOrderByPriceAsc(
            String productName,
            Double price
    );

    List<Product> getProductsByNameContainsAndPriceLessThan(
            String productName,
            Double price
    );

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) " +
            "AND p.price < :price " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) DESC")
    List<Product> getProductsByNameContainsAndPriceLessThanOrderByAverageRatingDesc(
            @Param("productName") String productName,
            @Param("price") Double price
    );

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) " +
            "AND p.price < :price " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) ASC")
    List<Product> getProductsByNameContainsAndPriceLessThanOrderByAverageRatingAsc(
            @Param("productName") String productName,
            @Param("price") Double price
    );

    List<Product> getProductsByNameContainsAndPriceLessThanOrderByPriceDesc(
            String productName,
            Double price
    );

    List<Product> getProductsByNameContainsAndPriceLessThanOrderByPriceAsc(
            String productName,
            Double price
    );

    List<Product> getProductsByNameContainsAndShopId(
            String productName,
            Long shopId
    );

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) " +
            "AND p.shop.id = :shopId " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) DESC")
    List<Product> getProductsByNameContainsAndShopIdOrderByAverageRatingDesc(
            @Param("productName") String productName,
            @Param("shopId") Long shopId
    );

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) " +
            "AND p.shop.id = :shopId " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) ASC")
    List<Product> getProductsByNameContainsAndShopIdOrderByAverageRatingAsc(
            @Param("productName") String productName,
            @Param("shopId") Long shopId
    );

    List<Product> getProductsByNameContainsAndShopIdOrderByPriceDesc(
            String productName,
            Long shopId
    );

    List<Product> getProductsByNameContainsAndShopIdOrderByPriceAsc(
            String productName,
            Long shopId
    );

    List<Product> getProductsByNameContainsAndShopIdAndPriceBetween(
            String productName,
            Long shopId,
            Double minimalPrice,
            Double maximalPrice
    );

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) " +
            "AND p.shop.id = :shopId " +
            "AND p.price BETWEEN :minimalPrice AND :maximalPrice " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) DESC")
    List<Product> getProductsByNameContainsAndShopIdAndPriceBetweenOrderByAverageRatingDesc(
            @Param("productName") String productName,
            @Param("shopId") Long shopId,
            @Param("minimalPrice") Double minimalPrice,
            @Param("maximalPrice") Double maximalPrice
    );

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) " +
            "AND p.shop.id = :shopId " +
            "AND p.price BETWEEN :minimalPrice AND :maximalPrice " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) ASC")
    List<Product> getProductsByNameContainsAndShopIdAndPriceBetweenOrderByAverageRatingAsc(
            @Param("productName") String productName,
            @Param("shopId") Long shopId,
            @Param("minimalPrice") Double minimalPrice,
            @Param("maximalPrice") Double maximalPrice
    );

    List<Product> getProductsByNameContainsAndShopIdAndPriceBetweenOrderByPriceDesc(
            String productName,
            Long shopId,
            Double minimalPrice,
            Double maximalPrice
    );

    List<Product> getProductsByNameContainsAndShopIdAndPriceBetweenOrderByPriceAsc(
            String productName,
            Long shopId,
            Double minimalPrice,
            Double maximalPrice
    );

    List<Product> getProductsByNameContainsAndShopIdAndPriceGreaterThan(String productName, Long shopId, Double price);

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) " +
            "AND p.shop.id = :shopId " +
            "AND p.price > :price " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) DESC")
    List<Product> getProductsByNameContainsAndShopIdAndPriceGreaterThanOrderByAverageRatingDesc(
            @Param("productName") String productName,
            @Param("shopId") Long shopId,
            @Param("price") Double price
    );

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) " +
            "AND p.shop.id = :shopId " +
            "AND p.price > :price " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) ASC")
    List<Product> getProductsByNameContainsAndShopIdAndPriceGreaterThanOrderByAverageRatingAsc(
            @Param("productName") String productName,
            @Param("shopId") Long shopId,
            @Param("price") Double price
    );

    List<Product> getProductsByNameContainsAndShopIdAndPriceGreaterThanOrderByPriceDesc(
            String productName,
            Long shopId,
            Double price
    );

    List<Product> getProductsByNameContainsAndShopIdAndPriceGreaterThanOrderByPriceAsc(
            String productName,
            Long shopId,
            Double price
    );

    List<Product> getProductsByNameContainsAndShopIdAndPriceLessThan(String productName, Long shopId, Double price);

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) " +
            "AND p.shop.id = :shopId " +
            "AND p.price < :price " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) DESC")
    List<Product> getProductsByNameContainsAndShopIdAndPriceLessThanOrderByAverageRatingDesc(
            @Param("productName") String productName,
            @Param("shopId") Long shopId,
            @Param("price") Double price
    );

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) " +
            "AND p.shop.id = :shopId " +
            "AND p.price < :price " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) ASC")
    List<Product> getProductsByNameContainsAndShopIdAndPriceLessThanOrderByAverageRatingAsc(
            @Param("productName") String productName,
            @Param("shopId") Long shopId,
            @Param("price") Double price
    );

    List<Product> getProductsByNameContainsAndShopIdAndPriceLessThanOrderByPriceDesc(
            String productName,
            Long shopId,
            Double price
    );

    List<Product> getProductsByNameContainsAndShopIdAndPriceLessThanOrderByPriceAsc(
            String productName,
            Long shopId,
            Double price
    );

    List<Product> getProductsByPriceBetween(Double minimalPrice, Double maximalPrice);

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE p.price BETWEEN :minimalPrice AND :maximalPrice " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) DESC")
    List<Product> getProductsByPriceBetweenOrderByAverageRatingDesc(
            @Param("minimalPrice") Double minimalPrice,
            @Param("maximalPrice") Double maximalPrice
    );

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE p.price BETWEEN :minimalPrice AND :maximalPrice " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) ASC")
    List<Product> getProductsByPriceBetweenOrderByAverageRatingAsc(
            @Param("minimalPrice") Double minimalPrice,
            @Param("maximalPrice") Double maximalPrice
    );

    List<Product> getProductsByPriceBetweenOrderByPriceDesc(Double minimalPrice, Double maximalPrice);

    List<Product> getProductsByPriceBetweenOrderByPriceAsc(Double minimalPrice, Double maximalPrice);

    List<Product> getProductsByPriceGreaterThan(Double price);

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE p.price > :price " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) DESC")
    List<Product> getProductsByPriceGreaterThanOrderByAverageRatingDesc(
            @Param("price") Double price
    );

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE p.price > :price " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) ASC")
    List<Product> getProductsByPriceGreaterThanOrderByAverageRatingAsc(
            @Param("price") Double price
    );

    List<Product> getProductsByPriceGreaterThanOrderByPriceDesc(Double price);

    List<Product> getProductsByPriceGreaterThanOrderByPriceAsc(Double price);

    List<Product> getProductsByPriceLessThan(Double price);

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE p.price < :price " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) DESC")
    List<Product> getProductsByPriceLessThanOrderByAverageRatingDesc(
            @Param("price") Double price
    );

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE p.price < :price " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) ASC")
    List<Product> getProductsByPriceLessThanOrderByAverageRatingAsc(
            @Param("price") Double price
    );

    List<Product> getProductsByPriceLessThanOrderByPriceDesc(Double price);

    List<Product> getProductsByPriceLessThanOrderByPriceAsc(Double price);

    List<Product> getProductsByShopIdAndPriceBetween(Long shopId, Double minimalPrice, Double maximalPrice);

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE p.shop.id = :shopId " +
            "AND p.price BETWEEN :minimalPrice AND :maximalPrice " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) DESC")
    List<Product> getProductsByShopIdAndPriceBetweenOrderByAverageRatingDesc(
            @Param("shopId") Long shopId,
            @Param("minimalPrice") Double minimalPrice,
            @Param("maximalPrice") Double maximalPrice
    );

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE p.shop.id = :shopId " +
            "AND p.price BETWEEN :minimalPrice AND :maximalPrice " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) ASC")
    List<Product> getProductsByShopIdAndPriceBetweenOrderByAverageRatingAsc(
            @Param("shopId") Long shopId,
            @Param("minimalPrice") Double minimalPrice,
            @Param("maximalPrice") Double maximalPrice
    );

    List<Product> getProductsByShopIdAndPriceBetweenOrderByPriceDesc(
            Long shopId,
            Double minimalPrice,
            Double maximalPrice
    );

    List<Product> getProductsByShopIdAndPriceBetweenOrderByPriceAsc(
            Long shopId,
            Double minimalPrice,
            Double maximalPrice
    );

    List<Product> getProductsByShopIdAndPriceGreaterThan(Long shopId, Double price);

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE p.shop.id = :shopId " +
            "AND p.price > :price " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) DESC")
    List<Product> getProductsByShopIdAndPriceGreaterThanOrderByAverageRatingDesc(
            @Param("shopId") Long shopId,
            @Param("price") Double price
    );

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE p.shop.id = :shopId " +
            "AND p.price > :price " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) ASC")
    List<Product> getProductsByShopIdAndPriceGreaterThanOrderByAverageRatingAsc(
            @Param("shopId") Long shopId,
            @Param("price") Double price
    );

    List<Product> getProductsByShopIdAndPriceGreaterThanOrderByPriceDesc(Long shopId, Double price);

    List<Product> getProductsByShopIdAndPriceGreaterThanOrderByPriceAsc(Long shopId, Double price);

    List<Product> getProductsByShopIdAndPriceLessThan(Long shopId, Double price);

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE p.shop.id = :shopId " +
            "AND p.price < :price " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) DESC")
    List<Product> getProductsByShopIdAndPriceLessThanOrderByAverageRatingDesc(
            @Param("shopId") Long shopId,
            @Param("price") Double price
    );

    @Query("SELECT p FROM Product p LEFT JOIN p.feedbacks f " +
            "WHERE p.shop.id = :shopId " +
            "AND p.price < :price " +
            "GROUP BY p.id " +
            "ORDER BY AVG(CAST(f.rate AS int)) ASC")
    List<Product> getProductsByShopIdAndPriceLessThanOrderByAverageRatingAsc(
            @Param("shopId") Long shopId,
            @Param("price") Double price
    );

    List<Product> getProductsByShopIdAndPriceLessThanOrderByPriceDesc(Long shopId, Double price);

    List<Product> getProductsByShopIdAndPriceLessThanOrderByPriceAsc(Long shopId, Double price);
}
