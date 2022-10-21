package com.cookingmama.cookingmamabackend.repository;

import com.cookingmama.cookingmamabackend.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Repository
@CrossOrigin(origins = "http://localhost:8080")
public interface CommentRepository extends JpaRepository<Comment, Long> {
//    List<Comment> findCommentByRecipeid(String recipeid);
}
