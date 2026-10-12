package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.JobPosting
import com.example.data.model.UserRole
import com.example.ui.theme.*

@Composable
fun JobDetailSheet(
    job: JobPosting,
    userRole: UserRole,
    onDismiss: () -> Unit,
    onApplyClick: (JobPosting) -> Unit,
    onDirectChatClick: (JobPosting) -> Unit,
    onToggleSave: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SkillspotDarkCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, SkillspotOutline),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(SkillspotYellow)
                            .border(1.dp, SkillspotBlack, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = job.company.take(2).uppercase(),
                            color = SkillspotBlack,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                    }

                    Row {
                        IconButton(onClick = { onToggleSave(job.id) }) {
                            Icon(
                                imageVector = if (job.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Save",
                                tint = if (job.isSaved) SkillspotYellow else SkillspotTextMuted
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = SkillspotTextMuted
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = job.title,
                    color = SkillspotTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = "${job.company} • ${if (job.isRemote) "${job.location} (Remote Available)" else job.location}",
                    color = SkillspotTextSecondary,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Stats strip
                Surface(
                    color = SkillspotDarkSurface,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SkillspotOutline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Compensation", color = SkillspotTextMuted, fontSize = 10.sp)
                            Text(job.stipendOrSalary, color = SkillspotYellow, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Duration", color = SkillspotTextMuted, fontSize = 10.sp)
                            Text(job.duration, color = SkillspotTextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Type", color = SkillspotTextMuted, fontSize = 10.sp)
                            Text(job.type.name, color = SkillspotYellow, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Industry Experience section
                Text("Industry Experience Gained:", color = SkillspotYellow, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = job.experienceGained,
                    color = SkillspotTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Mentorship section
                Text("Dedicated Mentorship:", color = SkillspotYellow, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SkillspotDarkSurface)
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.SupervisorAccount,
                        contentDescription = null,
                        tint = SkillspotYellow,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(job.mentorName, color = SkillspotTextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Text(job.mentorRole, color = SkillspotTextSecondary, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Full Role Description
                Text("Overview & Responsibilities:", color = SkillspotTextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = job.description,
                    color = SkillspotTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Required Skills
                Text("Required Tech Stack & Skills:", color = SkillspotTextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    job.requiredSkills.forEach { skill ->
                        Surface(
                            color = SkillspotElevatedSurface,
                            shape = RoundedCornerShape(4.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SkillspotOutline)
                        ) {
                            Text(
                                text = skill,
                                color = SkillspotTextPrimary,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Bottom Buttons: One-tap apply or Direct chat
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onDirectChatClick(job) },
                        border = androidx.compose.foundation.BorderStroke(1.dp, SkillspotOutline),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SkillspotTextPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.Chat, contentDescription = null, tint = SkillspotYellow, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Chat Recruiter", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }

                    if (userRole == UserRole.EMPLOYEE) {
                        Button(
                            onClick = { onApplyClick(job) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SkillspotYellow,
                                contentColor = SkillspotBlack
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("modal_apply_now_button")
                        ) {
                            Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("One-Tap Apply", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
