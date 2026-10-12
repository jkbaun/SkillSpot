package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.JobApplication
import com.example.data.model.ApplicationStatus
import com.example.data.model.UserRole
import com.example.ui.theme.*

@Composable
fun ApplicationsScreen(
    applications: List<JobApplication>,
    userRole: UserRole,
    onStatusChange: (applicationId: String, newStatus: ApplicationStatus) -> Unit,
    onMessageApplicant: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize().background(SkillspotBlack)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // Header
            item {
                Surface(
                    color = SkillspotDarkCard,
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SkillspotOutline),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(SkillspotYellowContainer)
                                .border(1.dp, SkillspotYellow, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (userRole == UserRole.EMPLOYEE) Icons.Default.Assignment else Icons.Default.FactCheck,
                                contentDescription = null,
                                tint = SkillspotYellow,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (userRole == UserRole.EMPLOYEE)
                                    "Your Active Internship Applications"
                                else
                                    "Candidate Application Pipeline",
                                color = SkillspotYellow,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = if (userRole == UserRole.EMPLOYEE)
                                    "Track direct employer review and interview invites in real-time."
                                else
                                    "Review submitted portfolio projects and advance candidates to interview.",
                                color = SkillspotTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            if (applications.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.AssignmentLate,
                                contentDescription = null,
                                tint = SkillspotTextMuted,
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (userRole == UserRole.EMPLOYEE) "No active applications" else "No applicants yet",
                                color = SkillspotTextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = if (userRole == UserRole.EMPLOYEE)
                                    "Browse opportunities and submit one-tap applications"
                                else
                                    "Applicants will appear here once candidates apply",
                                color = SkillspotTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            } else {
                items(applications, key = { it.id }) { app ->
                    ApplicationCard(
                        application = app,
                        userRole = userRole,
                        onStatusChange = onStatusChange,
                        onMessageApplicant = onMessageApplicant,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ApplicationCard(
    application: JobApplication,
    userRole: UserRole,
    onStatusChange: (String, ApplicationStatus) -> Unit,
    onMessageApplicant: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, SkillspotOutline, RoundedCornerShape(16.dp))
            .testTag("app_card_${application.id}"),
        colors = CardDefaults.cardColors(containerColor = SkillspotDarkCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Job title & Status badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = application.jobTitle,
                        color = SkillspotTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "${application.company} • Applied ${application.appliedDate}",
                        color = SkillspotTextSecondary,
                        fontSize = 11.sp
                    )
                }

                // Status chip
                val statusColor = when (application.status) {
                    ApplicationStatus.UNDER_REVIEW -> SkillspotTextSecondary
                    ApplicationStatus.SHORTLISTED -> SkillspotYellow
                    ApplicationStatus.INTERVIEW_SCHEDULED -> SkillspotYellowBright
                    ApplicationStatus.OFFER_EXTENDED -> SkillspotGreen
                    ApplicationStatus.ACCEPTED -> SkillspotGreen
                }

                Surface(
                    color = SkillspotElevatedSurface,
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = application.status.name.replace('_', ' '),
                        color = statusColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Candidate Pitch
            Surface(
                color = SkillspotDarkSurface,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SkillspotOutline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "Candidate: ${application.candidateName} (${application.candidateHeadline})",
                        color = SkillspotYellow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "\"${application.pitch}\"",
                        color = SkillspotTextSecondary,
                        fontSize = 12.sp
                    )
                    if (application.attachedProjectTitle.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FolderSpecial,
                                contentDescription = null,
                                tint = SkillspotYellow,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Attached: ${application.attachedProjectTitle}",
                                color = SkillspotTextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // If Employer: Recruitment pipeline management controls
            if (userRole == UserRole.EMPLOYER) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedButton(
                        onClick = { onMessageApplicant(application.candidateName, application.jobTitle) },
                        border = androidx.compose.foundation.BorderStroke(1.dp, SkillspotOutline),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SkillspotTextPrimary),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.weight(1f).height(36.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Chat, contentDescription = null, tint = SkillspotYellow, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Message", fontSize = 11.sp)
                    }

                    Button(
                        onClick = {
                            val nextStatus = when (application.status) {
                                ApplicationStatus.UNDER_REVIEW -> ApplicationStatus.SHORTLISTED
                                ApplicationStatus.SHORTLISTED -> ApplicationStatus.INTERVIEW_SCHEDULED
                                ApplicationStatus.INTERVIEW_SCHEDULED -> ApplicationStatus.OFFER_EXTENDED
                                else -> ApplicationStatus.ACCEPTED
                            }
                            onStatusChange(application.id, nextStatus)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SkillspotYellow,
                            contentColor = SkillspotBlack
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.weight(1.3f).height(36.dp)
                    ) {
                        Text(
                            text = when (application.status) {
                                ApplicationStatus.UNDER_REVIEW -> "Shortlist"
                                ApplicationStatus.SHORTLISTED -> "Invite Interview"
                                ApplicationStatus.INTERVIEW_SCHEDULED -> "Extend Offer"
                                else -> "Accepted"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
