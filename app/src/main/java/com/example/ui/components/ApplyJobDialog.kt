package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.JobPosting
import com.example.data.model.PortfolioProject
import com.example.ui.theme.*

@Composable
fun ApplyJobDialog(
    job: JobPosting,
    availableProjects: List<PortfolioProject>,
    onDismiss: () -> Unit,
    onSubmit: (JobPosting, String, String) -> Unit
) {
    var pitch by remember {
        mutableStateOf("I've built real-world apps with ${job.requiredSkills.take(2).joinToString(" & ")} and I'm eager to contribute to ${job.company}'s team and gain industry experience.")
    }
    var selectedProject by remember {
        mutableStateOf(availableProjects.firstOrNull()?.title ?: "Skillspot Mobile Client")
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SkillspotDarkCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, SkillspotOutline),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Direct Application",
                            color = SkillspotYellow,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "${job.title} • ${job.company}",
                            color = SkillspotTextSecondary,
                            fontSize = 12.sp
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

                Spacer(modifier = Modifier.height(14.dp))

                // Highlighted Mentorship & Stipend
                Surface(
                    color = SkillspotDarkSurface,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SkillspotOutline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Compensation: ${job.stipendOrSalary}",
                                color = SkillspotTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Mentored by ${job.mentorName} (${job.mentorRole})",
                                color = SkillspotYellow,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Pitch Text Field
                Text(
                    text = "Your Pitch to Hiring Manager:",
                    color = SkillspotTextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = pitch,
                    onValueChange = { pitch = it },
                    minLines = 3,
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SkillspotDarkSurface,
                        unfocusedContainerColor = SkillspotDarkSurface,
                        focusedBorderColor = SkillspotYellow,
                        unfocusedBorderColor = SkillspotOutline,
                        focusedTextColor = SkillspotTextPrimary,
                        unfocusedTextColor = SkillspotTextPrimary,
                        cursorColor = SkillspotYellow
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("application_pitch_input")
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Attach Portfolio Project
                Text(
                    text = "Attach Highlighting Portfolio Project:",
                    color = SkillspotTextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    availableProjects.take(3).forEach { project ->
                        val isSelected = selectedProject == project.title
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) SkillspotYellowContainer else SkillspotDarkSurface)
                                .border(
                                    1.dp,
                                    if (isSelected) SkillspotYellow else SkillspotOutline,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedProject = project.title }
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.FolderSpecial,
                                contentDescription = null,
                                tint = if (isSelected) SkillspotYellow else SkillspotTextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = project.title,
                                    color = if (isSelected) SkillspotYellow else SkillspotTextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = project.impactMetric,
                                    color = SkillspotTextSecondary,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Submit Button
                Button(
                    onClick = { onSubmit(job, pitch, selectedProject) },
                    enabled = pitch.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SkillspotYellow,
                        contentColor = SkillspotBlack
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("submit_application_button")
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Submit Direct Application",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
