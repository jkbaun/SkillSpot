package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.MainNavigationTab
import com.example.ui.theme.*

data class NavItem(
    val tab: MainNavigationTab,
    val title: String,
    val icon: ImageVector,
    val testTag: String
)

@Composable
fun SkillspotBottomNav(
    currentRole: UserRole,
    currentTab: MainNavigationTab,
    onTabSelected: (MainNavigationTab) -> Unit,
    unreadMessagesCount: Int = 0,
    modifier: Modifier = Modifier
) {
    val items = if (currentRole == UserRole.EMPLOYEE) {
        listOf(
            NavItem(MainNavigationTab.JOBS, "Opportunities", Icons.Default.Work, "nav_tab_jobs"),
            NavItem(MainNavigationTab.PORTFOLIO, "Portfolio", Icons.Default.FolderShared, "nav_tab_portfolio"),
            NavItem(MainNavigationTab.WORKSHOPS, "Workshops", Icons.Default.School, "nav_tab_workshops"),
            NavItem(MainNavigationTab.APPLICATIONS, "Applications", Icons.Default.Assignment, "nav_tab_applications"),
            NavItem(MainNavigationTab.MESSAGES, "Direct Chat", Icons.AutoMirrored.Filled.Chat, "nav_tab_messages")
        )
    } else {
        listOf(
            NavItem(MainNavigationTab.TALENT, "Scout Talent", Icons.Default.Groups, "nav_tab_talent"),
            NavItem(MainNavigationTab.JOBS, "My Postings", Icons.Default.WorkOutline, "nav_tab_postings"),
            NavItem(MainNavigationTab.APPLICATIONS, "Applicants", Icons.Default.FactCheck, "nav_tab_applicants"),
            NavItem(MainNavigationTab.MESSAGES, "Direct Chat", Icons.AutoMirrored.Filled.Chat, "nav_tab_messages")
        )
    }

    NavigationBar(
        containerColor = SkillspotDarkSurface,
        tonalElevation = 8.dp,
        modifier = modifier
    ) {
        items.forEach { item ->
            val isSelected = currentTab == item.tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(item.tab) },
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.tab == MainNavigationTab.MESSAGES && unreadMessagesCount > 0) {
                                Badge(
                                    containerColor = SkillspotYellow,
                                    contentColor = SkillspotBlack
                                ) {
                                    Text(unreadMessagesCount.toString(), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    }
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SkillspotBlack,
                    selectedTextColor = SkillspotYellow,
                    indicatorColor = SkillspotYellow,
                    unselectedIconColor = SkillspotTextSecondary,
                    unselectedTextColor = SkillspotTextMuted
                ),
                modifier = Modifier.testTag(item.testTag)
            )
        }
    }
}
