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
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Candidate
import com.example.ui.components.CandidateCard
import com.example.ui.theme.*

@Composable
fun TalentScreen(
    candidates: List<Candidate>,
    searchQuery: String,
    selectedFilter: String,
    showSavedOnly: Boolean,
    onFilterSelected: (String) -> Unit,
    onCandidateClick: (Candidate) -> Unit,
    onRecruitClick: (Candidate) -> Unit,
    onMessageClick: (Candidate) -> Unit,
    onToggleBookmark: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val filters = listOf("All Talent", "Kotlin & Compose", "AI & Vision", "Cloud & Go", "Product Design", "Available Now")

    val filteredCandidates = candidates.filter { candidate ->
        val matchesSearch = searchQuery.isBlank() ||
                candidate.name.contains(searchQuery, ignoreCase = true) ||
                candidate.headline.contains(searchQuery, ignoreCase = true) ||
                candidate.skills.any { it.contains(searchQuery, ignoreCase = true) }

        val matchesFilter = when (selectedFilter) {
            "All Talent" -> true
            "Kotlin & Compose" -> candidate.skills.any { it.contains("Kotlin", ignoreCase = true) || it.contains("Compose", ignoreCase = true) }
            "AI & Vision" -> candidate.skills.any { it.contains("PyTorch", ignoreCase = true) || it.contains("Python", ignoreCase = true) || it.contains("AI", ignoreCase = true) }
            "Cloud & Go" -> candidate.skills.any { it.contains("Go", ignoreCase = true) || it.contains("Kubernetes", ignoreCase = true) || it.contains("Rust", ignoreCase = true) }
            "Product Design" -> candidate.skills.any { it.contains("Figma", ignoreCase = true) || it.contains("Design", ignoreCase = true) }
            "Available Now" -> candidate.availability.contains("Immediate", ignoreCase = true) || candidate.availability.contains("Now", ignoreCase = true)
            else -> true
        }

        val matchesSaved = !showSavedOnly || candidate.isBookmarked

        matchesSearch && matchesFilter && matchesSaved
    }

    Box(modifier = modifier.fillMaxSize().background(SkillspotBlack)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // Employer Talent Scout Spotlight
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
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = SkillspotYellow,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Direct Talent Recruitment",
                                color = SkillspotYellow,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Recruit pre-vetted specialists based on verified projects, portfolio, and code.",
                                color = SkillspotTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Filter Chips
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

            // Results count
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${filteredCandidates.size} Specialized Candidates Available",
                        color = SkillspotTextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (showSavedOnly) {
                        Text(
                            text = "Showing Bookmarked",
                            color = SkillspotYellow,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Candidates List
            if (filteredCandidates.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = SkillspotTextMuted,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No candidates match criteria",
                                color = SkillspotTextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Try clearing search or choosing another skill tag",
                                color = SkillspotTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            } else {
                items(filteredCandidates, key = { it.id }) { candidate ->
                    CandidateCard(
                        candidate = candidate,
                        onCandidateClick = onCandidateClick,
                        onRecruitClick = onRecruitClick,
                        onMessageClick = onMessageClick,
                        onToggleBookmark = onToggleBookmark,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}
