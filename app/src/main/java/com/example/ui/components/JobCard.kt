package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExperienceLevel
import com.example.data.model.JobPosting
import com.example.data.model.UserRole
import com.example.ui.theme.*

@Composable
fun JobCard(
    job: JobPosting,
    userRole: UserRole,
    onJobClick: (JobPosting) -> Unit,
    onApplyClick: (JobPosting) -> Unit,
    onToggleSave: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, SkillspotOutline, RoundedCornerShape(16.dp))
            .clickable { onJobClick(job) }
            .testTag("job_card_${job.id}"),
        colors = CardDefaults.cardColors(containerColor = SkillspotDarkCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top Row: Company, Type Badge, Bookmark
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SkillspotElevatedSurface)
                            .border(1.dp, SkillspotYellow.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = job.company.take(2).uppercase(),
                            color = SkillspotYellow,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = job.company,
                            color = SkillspotTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (job.isRemote) "${job.location} • Remote" else job.location,
                            color = SkillspotTextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                IconButton(
                    onClick = { onToggleSave(job.id) },
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("bookmark_job_${job.id}")
                ) {
                    Icon(
                        imageVector = if (job.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save job",
                        tint = if (job.isSaved) SkillspotYellow else SkillspotTextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Job Title
            Text(
                text = job.title,
                color = SkillspotTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Badges row: Type, Compensation, Duration
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Type badge
                Surface(
                    color = SkillspotYellowContainer,
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SkillspotYellow.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = when (job.type) {
                            ExperienceLevel.INTERNSHIP -> "INTERNSHIP"
                            ExperienceLevel.JUNIOR -> "JUNIOR ROLE"
                            ExperienceLevel.SPECIALIST -> "SPECIALIST"
                            ExperienceLevel.MID_LEVEL -> "MID-LEVEL"
                        },
                        color = SkillspotYellow,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                // Stipend / Compensation
                Surface(
                    color = SkillspotElevatedSurface,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = job.stipendOrSalary,
                        color = SkillspotTextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                // Duration
                Surface(
                    color = SkillspotElevatedSurface,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = job.duration,
                        color = SkillspotTextSecondary,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Industry Experience Highlights
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SkillspotDarkSurface)
                    .padding(10.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = "Experience",
                            tint = SkillspotYellow,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Industry Experience Gained:",
                            color = SkillspotYellow,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = job.experienceGained,
                        color = SkillspotTextSecondary,
                        fontSize = 12.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Required Skills Tags
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                job.requiredSkills.take(3).forEach { skill ->
                    Surface(
                        color = SkillspotElevatedSurface,
                        shape = RoundedCornerShape(4.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SkillspotOutline)
                    ) {
                        Text(
                            text = skill,
                            color = SkillspotTextPrimary,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                if (job.requiredSkills.size > 3) {
                    Text(
                        text = "+${job.requiredSkills.size - 3} more",
                        color = SkillspotTextMuted,
                        fontSize = 11.sp,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Mentor & Direct Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.SupervisorAccount,
                        contentDescription = "Mentor",
                        tint = SkillspotTextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Mentor: ${job.mentorName}",
                        color = SkillspotTextMuted,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (userRole == UserRole.EMPLOYEE) {
                    Button(
                        onClick = { onApplyClick(job) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SkillspotYellow,
                            contentColor = SkillspotBlack
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("apply_button_${job.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Apply",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "One-Tap Apply",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                } else {
                    OutlinedButton(
                        onClick = { onJobClick(job) },
                        border = androidx.compose.foundation.BorderStroke(1.dp, SkillspotYellow),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SkillspotYellow),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(
                            text = "${job.applicantsCount} Applicants",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
