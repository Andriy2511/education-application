package com.example.educationplatform.service.implementation;

import com.example.educationplatform.model.Review;
import com.example.educationplatform.model.User;
import com.example.educationplatform.repository.ReviewRepository;
import com.example.educationplatform.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService implements IReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review addReview(Review review){
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> findReviewByTutor(User tutor){
        return reviewRepository.findAllByTutor(tutor);
    }

    @Override
    public Double getAverageRatingByTutor(User tutor){
        return reviewRepository.getAverageRatingByTutor(tutor);
    }
}
