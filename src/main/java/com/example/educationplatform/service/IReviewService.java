package com.example.educationplatform.service;

import com.example.educationplatform.model.Review;
import com.example.educationplatform.model.User;

import java.util.List;

public interface IReviewService {
    Review addReview(Review review);

    List<Review> findReviewByTutor(User tutor);

    Double getAverageRatingByTutor(User tutor);
}
