package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SkillspotDatabase
import com.example.data.model.*
import com.example.data.repository.SkillspotRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class MainNavigationTab {
    JOBS,          // Employee: Internships & Industry experience
    TALENT,        // Employer: Scout Specialized Talent
    PORTFOLIO,     // Employee: Build portfolio & skill credentials
    WORKSHOPS,     // Employee: Workshops & Certification details
    APPLICATIONS,  // Employee or Employer: Track hiring pipeline
    MESSAGES       // Direct connection chats
}

data class SkillspotUiState(
    val currentRole: UserRole = UserRole.EMPLOYEE,
    val currentTab: MainNavigationTab = MainNavigationTab.JOBS,
    val searchQuery: String = "",
    val selectedSkillFilter: String = "All",
    val showOnlySaved: Boolean = false,
    val jobs: List<JobPosting> = emptyList(),
    val candidates: List<Candidate> = emptyList(),
    val portfolioProjects: List<PortfolioProject> = emptyList(),
    val applications: List<JobApplication> = emptyList(),
    val workshops: List<Workshop> = emptyList(),
    val messages: List<DirectMessage> = emptyList(),
    val activeConversationId: String = "conv_novatech",
    val activeConversationRecipient: String = "Sarah Chen (NovaTech Labs)",
    val selectedJobForDetail: JobPosting? = null,
    val selectedCandidateForDetail: Candidate? = null,
    val isApplyDialogOpen: Boolean = false,
    val isAddProjectDialogOpen: Boolean = false,
    val isPostJobDialogOpen: Boolean = false,
    val isRecruitTalentDialogOpen: Boolean = false,
    val bannerMessage: String? = null
)

class SkillspotViewModel(application: android.app.Application) : AndroidViewModel(application) {

    private val repository: SkillspotRepository

    private val _uiState = MutableStateFlow(SkillspotUiState())
    val uiState: StateFlow<SkillspotUiState> = _uiState.asStateFlow()

    init {
        val db = SkillspotDatabase.getDatabase(application)
        repository = SkillspotRepository(db.dao())

        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }

        // Collect jobs
        viewModelScope.launch {
            repository.getJobs().collect { jobsList ->
                _uiState.update { it.copy(jobs = jobsList) }
            }
        }

        // Collect candidates
        viewModelScope.launch {
            repository.getCandidates().collect { candidatesList ->
                _uiState.update { it.copy(candidates = candidatesList) }
            }
        }

        // Collect portfolio
        viewModelScope.launch {
            repository.getPortfolioProjects().collect { projectsList ->
                _uiState.update { it.copy(portfolioProjects = projectsList) }
            }
        }

        // Collect applications
        viewModelScope.launch {
            repository.getApplications().collect { appsList ->
                _uiState.update { it.copy(applications = appsList) }
            }
        }

        // Collect workshops
        viewModelScope.launch {
            repository.getWorkshops().collect { workshopsList ->
                _uiState.update { it.copy(workshops = workshopsList) }
            }
        }

        // Collect messages
        viewModelScope.launch {
            repository.getMessages().collect { messagesList ->
                _uiState.update { it.copy(messages = messagesList) }
            }
        }
    }

    fun switchRole(role: UserRole) {
        _uiState.update {
            val nextTab = if (role == UserRole.EMPLOYER) {
                if (it.currentTab == MainNavigationTab.PORTFOLIO || it.currentTab == MainNavigationTab.WORKSHOPS) {
                    MainNavigationTab.TALENT
                } else it.currentTab
            } else {
                if (it.currentTab == MainNavigationTab.TALENT) {
                    MainNavigationTab.JOBS
                } else it.currentTab
            }
            it.copy(
                currentRole = role,
                currentTab = nextTab,
                bannerMessage = if (role == UserRole.EMPLOYER)
                    "Switched to Recruiter Mode: Scout talent, post roles & hire directly"
                else
                    "Switched to Talent Mode: Discover internships, build portfolio & apply"
            )
        }
    }

    fun selectTab(tab: MainNavigationTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun selectSkillFilter(filter: String) {
        _uiState.update { it.copy(selectedSkillFilter = filter) }
    }

    fun toggleShowOnlySaved() {
        _uiState.update { it.copy(showOnlySaved = !it.showOnlySaved) }
    }

    fun toggleSaveJob(jobId: String) {
        val current = _uiState.value.jobs.find { it.id == jobId }?.isSaved ?: false
        viewModelScope.launch {
            repository.toggleSaveJob(jobId, current)
        }
    }

    fun toggleBookmarkCandidate(candidateId: String) {
        val current = _uiState.value.candidates.find { it.id == candidateId }?.isBookmarked ?: false
        viewModelScope.launch {
            repository.toggleBookmarkCandidate(candidateId, current)
        }
    }

    fun openJobDetail(job: JobPosting) {
        _uiState.update { it.copy(selectedJobForDetail = job) }
    }

    fun closeJobDetail() {
        _uiState.update { it.copy(selectedJobForDetail = null) }
    }

    fun openApplyDialog(job: JobPosting) {
        _uiState.update { it.copy(selectedJobForDetail = job, isApplyDialogOpen = true) }
    }

    fun closeApplyDialog() {
        _uiState.update { it.copy(isApplyDialogOpen = false) }
    }

    fun openCandidateDetail(candidate: Candidate) {
        _uiState.update { it.copy(selectedCandidateForDetail = candidate) }
    }

    fun closeCandidateDetail() {
        _uiState.update { it.copy(selectedCandidateForDetail = null) }
    }

    fun openAddProjectDialog() {
        _uiState.update { it.copy(isAddProjectDialogOpen = true) }
    }

    fun closeAddProjectDialog() {
        _uiState.update { it.copy(isAddProjectDialogOpen = false) }
    }

    fun openPostJobDialog() {
        _uiState.update { it.copy(isPostJobDialogOpen = true) }
    }

    fun closePostJobDialog() {
        _uiState.update { it.copy(isPostJobDialogOpen = false) }
    }

    fun openRecruitTalentDialog(candidate: Candidate) {
        _uiState.update { it.copy(selectedCandidateForDetail = candidate, isRecruitTalentDialogOpen = true) }
    }

    fun closeRecruitTalentDialog() {
        _uiState.update { it.copy(isRecruitTalentDialogOpen = false) }
    }

    fun submitApplication(job: JobPosting, pitch: String, attachedProject: String) {
        viewModelScope.launch {
            repository.submitApplication(
                job = job,
                candidateName = "Alex Rivera",
                candidateHeadline = "Mobile Developer & UI Systems Enthusiast",
                pitch = pitch,
                attachedProjectTitle = attachedProject
            )
            _uiState.update {
                it.copy(
                    isApplyDialogOpen = false,
                    selectedJobForDetail = null,
                    bannerMessage = "Application submitted directly to ${job.company}!"
                )
            }
        }
    }

    fun updateApplicationStatus(applicationId: String, newStatus: ApplicationStatus) {
        viewModelScope.launch {
            repository.updateApplicationStatus(applicationId, newStatus)
            _uiState.update {
                it.copy(bannerMessage = "Candidate status updated to: ${newStatus.name.replace('_', ' ')}")
            }
        }
    }

    fun addPortfolioProject(
        title: String,
        role: String,
        description: String,
        techStack: List<String>,
        impactMetric: String,
        liveUrl: String,
        githubUrl: String
    ) {
        viewModelScope.launch {
            repository.addPortfolioProject(
                title = title,
                role = role,
                description = description,
                techStack = techStack,
                impactMetric = impactMetric,
                liveUrl = liveUrl,
                githubUrl = githubUrl
            )
            _uiState.update {
                it.copy(
                    isAddProjectDialogOpen = false,
                    bannerMessage = "Project added to your public portfolio!"
                )
            }
        }
    }

    fun deletePortfolioProject(id: String) {
        viewModelScope.launch {
            repository.deletePortfolioProject(id)
            _uiState.update { it.copy(bannerMessage = "Project removed from portfolio") }
        }
    }

    fun postJob(
        title: String,
        company: String,
        location: String,
        isRemote: Boolean,
        type: ExperienceLevel,
        stipendOrSalary: String,
        duration: String,
        requiredSkills: List<String>,
        description: String,
        experienceGained: String,
        mentorName: String,
        mentorRole: String
    ) {
        viewModelScope.launch {
            repository.postJob(
                title = title,
                company = company,
                location = location,
                isRemote = isRemote,
                type = type,
                stipendOrSalary = stipendOrSalary,
                duration = duration,
                requiredSkills = requiredSkills,
                description = description,
                experienceGained = experienceGained,
                mentorName = mentorName,
                mentorRole = mentorRole
            )
            _uiState.update {
                it.copy(
                    isPostJobDialogOpen = false,
                    bannerMessage = "Opportunity published to Skillspot candidates!"
                )
            }
        }
    }

    fun enrollInWorkshop(workshop: Workshop) {
        viewModelScope.launch {
            repository.enrollInWorkshop(workshop.id)
            _uiState.update {
                it.copy(bannerMessage = "Enrolled! Verified Certificate issued: ${workshop.certificateName}")
            }
        }
    }

    fun startDirectChat(recipientName: String, jobTitleContext: String, initialMessage: String? = null) {
        val convId = "conv_" + recipientName.filter { it.isLetterOrDigit() }.lowercase().take(12)
        _uiState.update {
            it.copy(
                activeConversationId = convId,
                activeConversationRecipient = recipientName,
                currentTab = MainNavigationTab.MESSAGES
            )
        }
        if (!initialMessage.isNullOrBlank()) {
            sendMessage(convId, initialMessage, recipientName, jobTitleContext)
        }
    }

    fun sendMessage(
        conversationId: String,
        text: String,
        recipientName: String,
        jobTitleContext: String = ""
    ) {
        if (text.isBlank()) return
        val senderRole = _uiState.value.currentRole
        val senderName = if (senderRole == UserRole.EMPLOYER) "NovaTech Recruiter" else "Alex Rivera"

        viewModelScope.launch {
            repository.sendMessage(
                conversationId = conversationId,
                senderName = senderName,
                senderRole = senderRole,
                recipientName = recipientName,
                text = text,
                jobTitleContext = jobTitleContext
            )
        }
    }

    fun clearBanner() {
        _uiState.update { it.copy(bannerMessage = null) }
    }
}
