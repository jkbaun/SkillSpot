package com.example.data.model

enum class UserRole {
    EMPLOYEE, // Seeking internships, gaining industry experience, building portfolio
    EMPLOYER  // Scouting specialized talent, posting internships/jobs, reviewing applicants
}

enum class ApplicationStatus {
    UNDER_REVIEW,
    SHORTLISTED,
    INTERVIEW_SCHEDULED,
    OFFER_EXTENDED,
    ACCEPTED
}

enum class ExperienceLevel {
    INTERNSHIP,
    JUNIOR,
    MID_LEVEL,
    SPECIALIST
}

data class JobPosting(
    val id: String,
    val title: String,
    val company: String,
    val location: String,
    val isRemote: Boolean,
    val type: ExperienceLevel,
    val stipendOrSalary: String,
    val duration: String,
    val requiredSkills: List<String>,
    val description: String,
    val experienceGained: String,
    val mentorName: String,
    val mentorRole: String,
    val applicantsCount: Int = 0,
    val isSaved: Boolean = false,
    val isPostedByUser: Boolean = false
)

data class Candidate(
    val id: String,
    val name: String,
    val headline: String,
    val avatarInitials: String,
    val location: String,
    val bio: String,
    val skills: List<String>,
    val yearsOfExperience: String,
    val availability: String,
    val completedProjectsCount: Int,
    val rating: Float,
    val isBookmarked: Boolean = false,
    val githubUrl: String = "",
    val portfolioUrl: String = ""
)

data class PortfolioProject(
    val id: String,
    val title: String,
    val role: String,
    val description: String,
    val techStack: List<String>,
    val impactMetric: String,
    val liveUrl: String = "",
    val githubUrl: String = "",
    val createdAt: String = "Recently"
)

data class JobApplication(
    val id: String,
    val jobId: String,
    val jobTitle: String,
    val company: String,
    val candidateName: String,
    val candidateHeadline: String,
    val pitch: String,
    val attachedProjectTitle: String,
    val status: ApplicationStatus,
    val appliedDate: String
)

data class Workshop(
    val id: String,
    val title: String,
    val instructor: String,
    val instructorTitle: String,
    val category: String,
    val duration: String,
    val skillsTaught: List<String>,
    val description: String,
    val certificateName: String,
    val credentialId: String = "",
    val isEnrolled: Boolean = false,
    val isCompleted: Boolean = false,
    val startDate: String = "Next Week"
)

data class DirectMessage(
    val id: String,
    val conversationId: String,
    val senderName: String,
    val senderRole: UserRole,
    val recipientName: String,
    val messageText: String,
    val timestamp: String,
    val jobTitleContext: String = ""
)
