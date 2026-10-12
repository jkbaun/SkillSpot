package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExperienceLevel
import com.example.data.model.JobPosting
import com.example.data.model.UserRole
import com.example.ui.components.JobCard
import com.example.ui.theme.*

@Composable
fun JobsScreen(
    jobs: List<JobPosting>,
    userRole: UserRole,
    searchQuery: String,
    selectedFilter: String,
    showSavedOnly: Boolean,
    onFilterSelected: (String) -> Unit,
    onJobClick: (JobPosting) -> Unit,
    onApplyClick: (JobPosting) -> Unit,
    onToggleSave: (String) -> Unit,
    onPostJobClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val filters = listOf("All", "Internships", "Junior", "Specialist", "Kotlin", "AI / Python", "Design", "Cloud")

    val filteredJobs = jobs.filter { job ->
        val matchesSearch = searchQuery.isBlank() ||
                job.title.contains(searchQuery, ignoreCase = true) ||
                job.company.contains(searchQuery, ignoreCase = true) ||
                job.requiredSkills.any { it.contains(searchQuery, ignoreCase = true) }

        val matchesFilter = when (selectedFilter) {
            "All" -> true
            "Internships" -> job.type == ExperienceLevel.INTERNSHIP
            "Junior" -> job.type == ExperienceLevel.JUNIOR
            "Specialist" -> job.type == ExperienceLevel.SPECIALIST
            "Kotlin" -> job.requiredSkills.any { it.contains("Kotlin", ignoreCase = true) }
            "AI / Python" -> job.requiredSkills.any { it.contains("Python", ignoreCase = true) || it.contains("AI", ignoreCase = true) }
            "Design" -> job.requiredSkills.any { it.contains("Design", ignoreCase = true) || it.contains("Figma", ignoreCase = true) }
            "Cloud" -> job.requiredSkills.any { it.contains("Cloud", ignoreCase = true) || it.contains("Kubernetes", ignoreCase = true) || it.contains("Go", ignoreCase = true) }
            else -> true
        }

        val matchesSaved = !showSavedOnly || job.isSaved

        matchesSearch && matchesFilter && matchesSaved
    }

    Box(modifier = modifier.fillMaxSize().background(SkillspotBlack)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // Hero Spotlight Header
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
                                imageVector = Icons.Default.ElectricBolt,
                                contentDescription = null,
                                tint = SkillspotYellow,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (userRole == UserRole.EMPLOYEE)
                                    "Direct Industry Experience Opportunities"
                                else
                                    "Manage Your Active Job & Internship Postings",
                                color = SkillspotYellow,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = if (userRole == UserRole.EMPLOYEE)
                                    "Connect directly with hiring managers & staff engineering mentors."
                                else
                                    "Review incoming applications and scout specialized candidates.",
                                color = SkillspotTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Filter Chips Horizontal Row
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filters) { filter ->
                        val isSelected = selectedFilter == filter
                        Surface(
                            color = if (isSelected) SkillspotYellow else SkillspotDarkCard,
                            shape = RoundedCornerShape(20.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) SkillspotYellow else SkillspotOutline
                            ),
                            modifier = Modifier.clickable { onFilterSelected(filter) }
                        ) {
                            Text(
                                text = filter,
                                color = if (isSelected) SkillspotBlack else SkillspotTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            // Results Counter
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${filteredJobs.size} Opportunities Found",
                        color = SkillspotTextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (showSavedOnly) {
                        Text(
                            text = "Showing Bookmarked Only",
                            color = SkillspotYellow,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // List of Job Cards
            if (filteredJobs.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = null,
                                tint = SkillspotTextMuted,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No matching opportunities",
                                color = SkillspotTextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Try searching for a different skill or clearing filters",
                                color = SkillspotTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            } else {
                items(filteredJobs, key = { it.id }) { job ->
                    JobCard(
                        job = job,
                        userRole = userRole,
                        onJobClick = onJobClick,
                        onApplyClick = onApplyClick,
                        onToggleSave = onToggleSave,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Floating Action Button if Recruiter / Employer mode
        if (userRole == UserRole.EMPLOYER) {
            FloatingActionButton(
                onClick = onPostJobClick,
                containerColor = SkillspotYellow,
                contentColor = SkillspotBlack,
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 100.dp, end = 20.dp)
                    .testTag("fab_post_job")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Post Opportunity"
                )
            }
        }
    }
}
