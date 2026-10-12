package com.example.ui.components

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.theme.*

@Composable
fun SkillspotTopBar(
    currentRole: UserRole,
    onRoleToggle: (UserRole) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    showSavedOnly: Boolean,
    onToggleShowSavedOnly: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = SkillspotBlack,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Header Row: Brand & Dual Role Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Brand with yellow spotlight symbol
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.testTag("skillspot_brand_header")
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SkillspotYellow),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Skillspot Icon",
                            tint = SkillspotBlack,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "SKILLSPOT",
                            color = SkillspotYellow,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = if (currentRole == UserRole.EMPLOYEE) "Talent Hub • Direct Connect" else "Recruiter Console • Direct Hire",
                            color = SkillspotTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Dual-Mode Toggle Capsule
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(SkillspotDarkCard)
                        .border(1.dp, SkillspotOutline, CircleShape)
                        .padding(3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val talentBg by animateColorAsState(
                        targetValue = if (currentRole == UserRole.EMPLOYEE) SkillspotYellow else Color.Transparent,
                        label = "talent_bg"
                    )
                    val talentText by animateColorAsState(
                        targetValue = if (currentRole == UserRole.EMPLOYEE) SkillspotBlack else SkillspotTextSecondary,
                        label = "talent_text"
                    )
                    val recruiterBg by animateColorAsState(
                        targetValue = if (currentRole == UserRole.EMPLOYER) SkillspotYellow else Color.Transparent,
                        label = "recruiter_bg"
                    )
                    val recruiterText by animateColorAsState(
                        targetValue = if (currentRole == UserRole.EMPLOYER) SkillspotBlack else SkillspotTextSecondary,
                        label = "recruiter_text"
                    )

                    // Employee / Talent Button
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(talentBg)
                            .clickable { onRoleToggle(UserRole.EMPLOYEE) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("switch_to_talent_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Talent",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = talentText
                        )
                    }

                    // Employer / Recruiter Button
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(recruiterBg)
                            .clickable { onRoleToggle(UserRole.EMPLOYER) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("switch_to_recruiter_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Recruiter",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = recruiterText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar & Bookmark Filter
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = {
                        Text(
                            text = if (currentRole == UserRole.EMPLOYEE)
                                "Search internships, roles, skills (e.g. Kotlin, AI)..."
                            else
                                "Search talent by skill, project, domain...",
                            fontSize = 13.sp,
                            color = SkillspotTextMuted
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = SkillspotYellow
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear search",
                                    tint = SkillspotTextMuted
                                )
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SkillspotDarkCard,
                        unfocusedContainerColor = SkillspotDarkCard,
                        focusedBorderColor = SkillspotYellow,
                        unfocusedBorderColor = SkillspotOutline,
                        focusedTextColor = SkillspotTextPrimary,
                        unfocusedTextColor = SkillspotTextPrimary,
                        cursorColor = SkillspotYellow
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("search_text_input")
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Bookmark filter button
                IconButton(
                    onClick = onToggleShowSavedOnly,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (showSavedOnly) SkillspotYellowContainer else SkillspotDarkCard)
                        .border(
                            1.dp,
                            if (showSavedOnly) SkillspotYellow else SkillspotOutline,
                            RoundedCornerShape(12.dp)
                        )
                        .testTag("saved_items_filter_toggle")
                ) {
                    Icon(
                        imageVector = if (showSavedOnly) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Saved Filter",
                        tint = if (showSavedOnly) SkillspotYellow else SkillspotTextSecondary
                    )
                }
            }
        }
    }
}
