package com.nopoverty.platform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "family_members")
public class FamilyMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 50)
    private String relationship; // SPOUSE, SON, DAUGHTER, FATHER, MOTHER, BROTHER, SISTER, OTHER

    @Column(nullable = false)
    private Integer age;

    @Column(length = 20)
    private String gender;

    @Column(length = 100)
    private String occupation;

    @Column(length = 100)
    private String education;

    @Column(precision = 12, scale = 2)
    private BigDecimal incomeContribution = BigDecimal.ZERO;

    private Boolean disabilityStatus = false;
    private Boolean studentStatus = false;
    private Boolean seniorCitizenStatus = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    public FamilyMember() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public BigDecimal getIncomeContribution() { return incomeContribution; }
    public void setIncomeContribution(BigDecimal incomeContribution) { this.incomeContribution = incomeContribution; }

    public Boolean getDisabilityStatus() { return disabilityStatus; }
    public void setDisabilityStatus(Boolean disabilityStatus) { this.disabilityStatus = disabilityStatus; }

    public Boolean getStudentStatus() { return studentStatus; }
    public void setStudentStatus(Boolean studentStatus) { this.studentStatus = studentStatus; }

    public Boolean getSeniorCitizenStatus() { return seniorCitizenStatus; }
    public void setSeniorCitizenStatus(Boolean seniorCitizenStatus) { this.seniorCitizenStatus = seniorCitizenStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

