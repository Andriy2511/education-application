package com.example.educationplatform.repository;

import com.example.educationplatform.model.Review;
import com.example.educationplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByTutor(User tutor);

    @Query("SELECT AVG(e.rate) FROM Review e WHERE e.tutor = ?1")
    Double getAverageRatingByTutor(User tutor);
}
