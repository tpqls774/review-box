package com.java.assessment.reviewbox.domain.review.persistence.repository;

import com.java.assessment.reviewbox.domain.review.persistence.entity.ReviewEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    @Query(
            """
                       select r from ReviewEntity r
                       join fetch r.movie
                       join fetch r.user
                       where r.id in :ids
                    """)
    List<ReviewEntity> findByIdIn(@Param("ids") List<Long> ids);

    @Query(
            """
                        SELECT r FROM ReviewEntity r
                        JOIN FETCH r.movie
                        WHERE r.user.id = :userId
                    """)
    Page<ReviewEntity> findByUserId(long userId, Pageable pageable);

    @Query(
            """
                    SELECT r FROM ReviewEntity r
                    JOIN FETCH r.user
                    WHERE r.movie.id = :movieId
                    """)
    Page<ReviewEntity> findByMovieIdWithUser(@Param("movieId") Long movieId, Pageable pageable);

    long countByMovieId(long movieId);
}
