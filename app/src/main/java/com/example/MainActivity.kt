package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.UserRole
import com.example.ui.MainNavigationTab
import com.example.ui.SkillspotViewModel
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SkillspotApp()
            }
        }
    }
}

@Composable
fun SkillspotApp(
    viewModel: SkillspotViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle back button behavior
    BackHandler(enabled = state.selectedJobForDetail != null ||
            state.selectedCandidateForDetail != null ||
            state.isApplyDialogOpen ||
            state.isAddProjectDialogOpen ||
            state.isPostJobDialogOpen ||
            state.isRecruitTalentDialogOpen ||
            (state.currentRole == UserRole.EMPLOYEE && state.currentTab != MainNavigationTab.JOBS) ||
            (state.currentRole == UserRole.EMPLOYER && state.currentTab != MainNavigationTab.TALENT)) {
        when {
            state.isApplyDialogOpen -> viewModel.closeApplyDialog()
            state.isAddProjectDialogOpen -> viewModel.closeAddProjectDialog()
            state.isPostJobDialogOpen -> viewModel.closePostJobDialog()
            state.isRecruitTalentDialogOpen -> viewModel.closeRecruitTalentDialog()
            state.selectedJobForDetail != null -> viewModel.closeJobDetail()
            state.selectedCandidateForDetail != null -> viewModel.closeCandidateDetail()
            state.currentRole == UserRole.EMPLOYEE -> viewModel.selectTab(MainNavigationTab.JOBS)
            state.currentRole == UserRole.EMPLOYER -> viewModel.selectTab(MainNavigationTab.TALENT)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = SkillspotBlack,
        topBar = {
            SkillspotTopBar(
                currentRole = state.currentRole,
                onRoleToggle = { viewModel.switchRole(it) },
                searchQuery = state.searchQuery,
                onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                showSavedOnly = state.showOnlySaved,
                onToggleShowSavedOnly = { viewModel.toggleShowOnlySaved() }
            )
        },
        bottomBar = {
            SkillspotBottomNav(
                currentRole = state.currentRole,
                currentTab = state.currentTab,
                onTabSelected = { viewModel.selectTab(it) },
                unreadMessagesCount = if (state.messages.isNotEmpty()) 1 else 0
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Screen Content based on Current Tab
            when (state.currentTab) {
                MainNavigationTab.JOBS -> {
                    JobsScreen(
                        jobs = state.jobs,
                        userRole = state.currentRole,
                        searchQuery = state.searchQuery,
                        selectedFilter = state.selectedSkillFilter,
                        showSavedOnly = state.showOnlySaved,
                        onFilterSelected = { viewModel.selectSkillFilter(it) },
                        onJobClick = { viewModel.openJobDetail(it) },
                        onApplyClick = { viewModel.openApplyDialog(it) },
                        onToggleSave = { viewModel.toggleSaveJob(it) },
                        onPostJobClick = { viewModel.openPostJobDialog() }
                    )
                }

                MainNavigationTab.TALENT -> {
                    TalentScreen(
                        candidates = state.candidates,
                        searchQuery = state.searchQuery,
                        selectedFilter = state.selectedSkillFilter,
                        showSavedOnly = state.showOnlySaved,
                        onFilterSelected = { viewModel.selectSkillFilter(it) },
                        onCandidateClick = { viewModel.openCandidateDetail(it) },
                        onRecruitClick = { viewModel.openRecruitTalentDialog(it) },
                        onMessageClick = { candidate ->
                            viewModel.startDirectChat(candidate.name, candidate.headline)
                        },
                        onToggleBookmark = { viewModel.toggleBookmarkCandidate(it) }
                    )
                }

                MainNavigationTab.PORTFOLIO -> {
                    PortfolioScreen(
                        projects = state.portfolioProjects,
                        workshops = state.workshops,
                        onAddProjectClick = { viewModel.openAddProjectDialog() },
                        onDeleteProject = { viewModel.deletePortfolioProject(it) }
                    )
                }

                MainNavigationTab.WORKSHOPS -> {
                    WorkshopsScreen(
                        workshops = state.workshops,
                        onEnrollClick = { viewModel.enrollInWorkshop(it) }
                    )
                }

                MainNavigationTab.APPLICATIONS -> {
                    ApplicationsScreen(
                        applications = state.applications,
                        userRole = state.currentRole,
                        onStatusChange = { appId, newStatus ->
                            viewModel.updateApplicationStatus(appId, newStatus)
                        },
                        onMessageApplicant = { candidateName, jobTitle ->
                            viewModel.startDirectChat(candidateName, jobTitle)
                        }
                    )
                }

                MainNavigationTab.MESSAGES -> {
                    MessagesScreen(
                        messages = state.messages,
                        currentRole = state.currentRole,
                        activeConversationId = state.activeConversationId,
                        activeRecipientName = state.activeConversationRecipient,
                        onSendMessage = { convId, text, recipient, jobTitle ->
                            viewModel.sendMessage(convId, text, recipient, jobTitle)
                        }
                    )
                }
            }

            // High-Contrast Interactive Toast Banner
            AnimatedVisibility(
                visible = state.bannerMessage != null,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 10.dp, start = 16.dp, end = 16.dp)
            ) {
                state.bannerMessage?.let { msg ->
                    Surface(
                        color = SkillspotYellow,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SkillspotBlack),
                        shadowElevation = 8.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = SkillspotBlack,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = msg,
                                color = SkillspotBlack,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { viewModel.clearBanner() },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Dismiss",
                                    tint = SkillspotBlack,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Sheets and Dialogs
    state.selectedJobForDetail?.let { job ->
        if (!state.isApplyDialogOpen) {
            JobDetailSheet(
                job = job,
                userRole = state.currentRole,
                onDismiss = { viewModel.closeJobDetail() },
                onApplyClick = { viewModel.openApplyDialog(it) },
                onDirectChatClick = {
                    viewModel.closeJobDetail()
                    viewModel.startDirectChat(job.company, job.title)
                },
                onToggleSave = { viewModel.toggleSaveJob(it) }
            )
        }
    }

    if (state.isApplyDialogOpen && state.selectedJobForDetail != null) {
        ApplyJobDialog(
            job = state.selectedJobForDetail!!,
            availableProjects = state.portfolioProjects,
            onDismiss = { viewModel.closeApplyDialog() },
            onSubmit = { job, pitch, project ->
                viewModel.submitApplication(job, pitch, project)
            }
        )
    }

    if (state.isAddProjectDialogOpen) {
        AddProjectDialog(
            onDismiss = { viewModel.closeAddProjectDialog() },
            onSubmit = { title, role, desc, techs, impact, live, git ->
                viewModel.addPortfolioProject(title, role, desc, techs, impact, live, git)
            }
        )
    }

    if (state.isPostJobDialogOpen) {
        PostJobDialog(
            onDismiss = { viewModel.closePostJobDialog() },
            onSubmit = { title, company, loc, remote, type, stipend, dur, skills, desc, exp, mentor, mentorRole ->
                viewModel.postJob(title, company, loc, remote, type, stipend, dur, skills, desc, exp, mentor, mentorRole)
            }
        )
    }

    if (state.isRecruitTalentDialogOpen && state.selectedCandidateForDetail != null) {
        RecruitTalentDialog(
            candidate = state.selectedCandidateForDetail!!,
            onDismiss = { viewModel.closeRecruitTalentDialog() },
            onSendOffer = { roleTitle, comp, message ->
                val cand = state.selectedCandidateForDetail!!
                viewModel.closeRecruitTalentDialog()
                viewModel.startDirectChat(
                    recipientName = cand.name,
                    jobTitleContext = roleTitle,
                    initialMessage = "Recruitment Offer: $roleTitle ($comp)\n$message"
                )
            }
        )
    }
}
