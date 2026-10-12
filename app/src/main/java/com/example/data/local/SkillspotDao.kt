package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SkillspotDao {

    // Jobs
    @Query("SELECT * FROM jobs ORDER BY id DESC")
    fun getAllJobs(): Flow<List<JobEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJobs(jobs: List<JobEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: JobEntity)

    @Query("UPDATE jobs SET isSaved = :isSaved WHERE id = :jobId")
    suspend fun updateJobSavedStatus(jobId: String, isSaved: Boolean)

    @Query("UPDATE jobs SET applicantsCount = applicantsCount + 1 WHERE id = :jobId")
    suspend fun incrementJobApplicantCount(jobId: String)

    // Candidates
    @Query("SELECT * FROM candidates")
    fun getAllCandidates(): Flow<List<CandidateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCandidates(candidates: List<CandidateEntity>)

    @Query("UPDATE candidates SET isBookmarked = :isBookmarked WHERE id = :candidateId")
    suspend fun updateCandidateBookmark(candidateId: String, isBookmarked: Boolean)

    // Portfolio
    @Query("SELECT * FROM portfolio_projects ORDER BY id DESC")
    fun getAllPortfolioProjects(): Flow<List<PortfolioEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPortfolioProjects(projects: List<PortfolioEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPortfolioProject(project: PortfolioEntity)

    @Query("DELETE FROM portfolio_projects WHERE id = :projectId")
    suspend fun deletePortfolioProject(projectId: String)

    // Applications
    @Query("SELECT * FROM applications ORDER BY appliedDate DESC")
    fun getAllApplications(): Flow<List<ApplicationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(application: ApplicationEntity)

    @Query("UPDATE applications SET status = :status WHERE id = :applicationId")
    suspend fun updateApplicationStatus(applicationId: String, status: String)

    // Workshops
    @Query("SELECT * FROM workshops")
    fun getAllWorkshops(): Flow<List<WorkshopEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkshops(workshops: List<WorkshopEntity>)

    @Query("UPDATE workshops SET isEnrolled = :isEnrolled, isCompleted = :isCompleted, credentialId = :credentialId WHERE id = :workshopId")
    suspend fun updateWorkshopEnrollment(workshopId: String, isEnrolled: Boolean, isCompleted: Boolean, credentialId: String)

    // Messages
    @Query("SELECT * FROM messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)
}
