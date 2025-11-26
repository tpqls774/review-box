package com.java.assessment.reviewbox.domain.review.persistence.repository;

import com.java.assessment.reviewbox.domain.review.persistence.entity.ReviewLikeEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewLikeRepository extends JpaRepository<ReviewLikeEntity, Long> {
    Optional<ReviewLikeEntity> findByUserIdAndReviewId(Long userId, Long reviewId);

    Page<ReviewLikeEntity> findByUserId(long userId, Pageable pageable);

    long countByReviewId(Long reviewId);

    @Query(
            """
                        SELECT rl.review.id AS reviewId, COUNT(rl.id) AS likes
                        FROM ReviewLikeEntity rl
                        WHERE rl.review.id IN :reviewIds
                        GROUP BY rl.review.id
                    """)
    List<ReviewLikesCount> countLikesGroupByReviewIds(@Param("reviewIds") List<Long> reviewIds);

    boolean existsByUserIdAndReviewId(Long userId, Long reviewId);

    void deleteByReviewId(Long reviewId);

    interface ReviewLikesCount {
        Long getReviewId();

        Long getLikes();
    }
}
