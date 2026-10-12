package com.example.ui.components

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*

@Composable
fun AddProjectDialog(
    onDismiss: () -> Unit,
    onSubmit: (
        title: String,
        role: String,
        description: String,
        techStack: List<String>,
        impactMetric: String,
        liveUrl: String,
        githubUrl: String
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("Lead Developer") }
    var description by remember { mutableStateOf("") }
    var techStackInput by remember { mutableStateOf("Kotlin, Compose, Room") }
    var impactMetric by remember { mutableStateOf("+35% performance gain, 2k downloads") }
    var liveUrl by remember { mutableStateOf("https://github.com/my-portfolio-project") }
    var githubUrl by remember { mutableStateOf("https://github.com/my-repo") }

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
                            text = "Build Portfolio Project",
                            color = SkillspotYellow,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Showcase your real-world work to employers",
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

                // Project Title
                Text("Project Title *", color = SkillspotTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("e.g. Distributed Cache & Raft Consensus", fontSize = 12.sp, color = SkillspotTextMuted) },
                    singleLine = true,
                    colors = customTextFieldColors(),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("project_title_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Role
                Text("Your Role in Project *", color = SkillspotTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = role,
                    onValueChange = { role = it },
                    singleLine = true,
                    colors = customTextFieldColors(),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("project_role_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Description
                Text("Project Overview & Problem Solved *", color = SkillspotTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Describe architecture, key challenges, and solutions...", fontSize = 12.sp, color = SkillspotTextMuted) },
                    minLines = 2,
                    maxLines = 4,
                    colors = customTextFieldColors(),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("project_description_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Tech Stack
                Text("Tech Stack (comma separated) *", color = SkillspotTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = techStackInput,
                    onValueChange = { techStackInput = it },
                    singleLine = true,
                    colors = customTextFieldColors(),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("project_tech_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Impact metric
                Text("Measurable Impact / Result *", color = SkillspotTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = impactMetric,
                    onValueChange = { impactMetric = it },
                    singleLine = true,
                    colors = customTextFieldColors(),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("project_impact_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Repo / Demo link
                Text("GitHub / Demo URL", color = SkillspotTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = liveUrl,
                    onValueChange = { liveUrl = it },
                    singleLine = true,
                    colors = customTextFieldColors(),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Save button
                Button(
                    onClick = {
                        val techs = techStackInput.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        onSubmit(
                            title,
                            role,
                            description.ifBlank { "Real-world project showcasing end-to-end architecture and clean coding practices." },
                            techs,
                            impactMetric,
                            liveUrl,
                            githubUrl
                        )
                    },
                    enabled = title.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SkillspotYellow,
                        contentColor = SkillspotBlack
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("save_portfolio_project_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Add to My Portfolio", fontWeight = FontWeight.Bold, fontSize = 13.sp)
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
