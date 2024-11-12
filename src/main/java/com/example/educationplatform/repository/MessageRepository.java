package com.example.educationplatform.repository;

import com.example.educationplatform.model.Message;
import com.example.educationplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByUserAndReceiverId(User user, Long receiverId);

    @Query("SELECT DISTINCT receiverId FROM Message WHERE user = ?1")
    List<Long> getUserChatsByReceiverId(User user);

    @Query("SELECT DISTINCT user.id FROM Message WHERE receiverId = ?1")
    List<Long> getDistinctUserIdByReceiverId(Long receiverId);
}
