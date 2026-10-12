package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jobs")
data class JobEntity(
    @PrimaryKey val id: String,
    val title: String,
    val company: String,
    val location: String,
    val isRemote: Boolean,
    val type: String, // INTERNSHIP, JUNIOR, etc.
    val stipendOrSalary: String,
    val duration: String,
    val requiredSkillsCsv: String,
    val description: String,
    val experienceGained: String,
    val mentorName: String,
    val mentorRole: String,
    val applicantsCount: Int,
    val isSaved: Boolean,
    val isPostedByUser: Boolean
)

@Entity(tableName = "candidates")
data class CandidateEntity(
    @PrimaryKey val id: String,
    val name: String,
    val headline: String,
    val avatarInitials: String,
    val location: String,
    val bio: String,
    val skillsCsv: String,
    val yearsOfExperience: String,
    val availability: String,
    val completedProjectsCount: Int,
    val rating: Float,
    val isBookmarked: Boolean,
    val githubUrl: String,
    val portfolioUrl: String
)

@Entity(tableName = "portfolio_projects")
data class PortfolioEntity(
    @PrimaryKey val id: String,
    val title: String,
    val role: String,
    val description: String,
    val techStackCsv: String,
    val impactMetric: String,
    val liveUrl: String,
    val githubUrl: String,
    val createdAt: String
)

@Entity(tableName = "applications")
data class ApplicationEntity(
    @PrimaryKey val id: String,
    val jobId: String,
    val jobTitle: String,
    val company: String,
    val candidateName: String,
    val candidateHeadline: String,
    val pitch: String,
    val attachedProjectTitle: String,
    val status: String,
    val appliedDate: String
)

@Entity(tableName = "workshops")
data class WorkshopEntity(
    @PrimaryKey val id: String,
    val title: String,
    val instructor: String,
    val instructorTitle: String,
    val category: String,
    val duration: String,
    val skillsTaughtCsv: String,
    val description: String,
    val certificateName: String,
    val credentialId: String,
    val isEnrolled: Boolean,
    val isCompleted: Boolean,
    val startDate: String
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val senderName: String,
    val senderRole: String,
    val recipientName: String,
    val messageText: String,
    val timestamp: String,
    val jobTitleContext: String
)
