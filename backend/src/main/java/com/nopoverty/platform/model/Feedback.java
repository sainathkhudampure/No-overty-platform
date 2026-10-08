package com.nopoverty.platform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "scheme_id")
    private Scheme scheme;

    @Column(nullable = false)
    private Integer rating; // 1-5

    private Integer usefulnessRating; // 1-5
    private Integer easeRating; // 1-5

    @Column(columnDefinition = "TEXT")
    private String comments;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Feedback() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Scheme getScheme() { return scheme; }
    public void setScheme(Scheme scheme) { this.scheme = scheme; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public Integer getUsefulnessRating() { return usefulnessRating; }
    public void setUsefulnessRating(Integer usefulnessRating) { this.usefulnessRating = usefulnessRating; }

    public Integer getEaseRating() { return easeRating; }
    public void setEaseRating(Integer easeRating) { this.easeRating = easeRating; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

