package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
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
import com.example.data.model.ExperienceLevel
import com.example.ui.theme.*

@Composable
fun PostJobDialog(
    onDismiss: () -> Unit,
    onSubmit: (
        title: String,
        company: String,
        location: String,
        isRemote: Boolean,
        type: ExperienceLevel,
        stipend: String,
        duration: String,
        skills: List<String>,
        description: String,
        experienceGained: String,
        mentorName: String,
        mentorRole: String
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("NovaTech Labs") }
    var location by remember { mutableStateOf("San Francisco, CA") }
    var isRemote by remember { mutableStateOf(true) }
    var selectedType by remember { mutableStateOf(ExperienceLevel.INTERNSHIP) }
    var stipend by remember { mutableStateOf("$45 / hr") }
    var duration by remember { mutableStateOf("12 Weeks") }
    var skillsInput by remember { mutableStateOf("Kotlin, Compose, Coroutines") }
    var description by remember { mutableStateOf("Work with our mobile team to develop production-grade features.") }
    var experienceGained by remember { mutableStateOf("Direct mentorship from staff engineers, architectural code reviews, shipping to production.") }
    var mentorName by remember { mutableStateOf("Marcus Brody") }
    var mentorRole by remember { mutableStateOf("Senior Tech Lead") }

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
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Post Opportunity",
                            color = SkillspotYellow,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Connect directly with specialized talent",
                            color = SkillspotTextSecondary,
                            fontSize = 11.sp
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

                // Title
                Text("Role Title *", color = SkillspotTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("e.g. Android & Compose Engineering Intern", fontSize = 12.sp, color = SkillspotTextMuted) },
                    singleLine = true,
                    colors = customTextFieldColors(),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("job_title_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Role Type Selection
                Text("Opportunity Type *", color = SkillspotTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ExperienceLevel.entries.forEach { type ->
                        val isSelected = selectedType == type
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) SkillspotYellow else SkillspotDarkSurface)
                                .clickable { selectedType = type }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = type.name.take(4),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) SkillspotBlack else SkillspotTextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Company & Location
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Company *", color = SkillspotTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = company,
                            onValueChange = { company = it },
                            singleLine = true,
                            colors = customTextFieldColors(),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Location *", color = SkillspotTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = location,
                            onValueChange = { location = it },
                            singleLine = true,
                            colors = customTextFieldColors(),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Stipend & Duration
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Stipend / Salary *", color = SkillspotTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = stipend,
                            onValueChange = { stipend = it },
                            singleLine = true,
                            colors = customTextFieldColors(),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().testTag("job_stipend_input")
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Duration *", color = SkillspotTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = duration,
                            onValueChange = { duration = it },
                            singleLine = true,
                            colors = customTextFieldColors(),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Skills required
                Text("Required Skills (comma separated) *", color = SkillspotTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = skillsInput,
                    onValueChange = { skillsInput = it },
                    singleLine = true,
                    colors = customTextFieldColors(),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("job_skills_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Industry Experience Gained
                Text("Industry Experience Gained *", color = SkillspotTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = experienceGained,
                    onValueChange = { experienceGained = it },
                    minLines = 2,
                    colors = customTextFieldColors(),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Mentor details
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Mentor Name", color = SkillspotTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = mentorName,
                            onValueChange = { mentorName = it },
                            singleLine = true,
                            colors = customTextFieldColors(),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Mentor Role", color = SkillspotTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = mentorRole,
                            onValueChange = { mentorRole = it },
                            singleLine = true,
                            colors = customTextFieldColors(),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Submit Button
                Button(
                    onClick = {
                        val skillsList = skillsInput.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        onSubmit(
                            title,
                            company,
                            location,
                            isRemote,
                            selectedType,
                            stipend,
                            duration,
                            skillsList,
                            description,
                            experienceGained,
                            mentorName,
                            mentorRole
                        )
                    },
                    enabled = title.isNotBlank() && company.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SkillspotYellow,
                        contentColor = SkillspotBlack
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("publish_opportunity_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Publish Opportunity Directly", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun customTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = SkillspotDarkSurface,
    unfocusedContainerColor = SkillspotDarkSurface,
    focusedBorderColor = SkillspotYellow,
    unfocusedBorderColor = SkillspotOutline,
    focusedTextColor = SkillspotTextPrimary,
    unfocusedTextColor = SkillspotTextPrimary,
    cursorColor = SkillspotYellow
)
