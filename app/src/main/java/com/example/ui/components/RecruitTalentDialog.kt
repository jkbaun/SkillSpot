package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Candidate
import com.example.ui.theme.*

@Composable
fun RecruitTalentDialog(
    candidate: Candidate,
    onDismiss: () -> Unit,
    onSendOffer: (roleTitle: String, compensation: String, message: String) -> Unit
) {
    var roleTitle by remember { mutableStateOf("Android & Compose Engineering Intern") }
    var compensation by remember { mutableStateOf("$50 / hr + Benefits") }
    var inviteMessage by remember {
        mutableStateOf("Hi ${candidate.name}, we reviewed your specialized skills in ${candidate.skills.take(2).joinToString(" and ")} and your impressive portfolio. We'd love to extend a direct interview offer for our team!")
    }

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
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Recruit Talent Directly",
                            color = SkillspotYellow,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "To ${candidate.name} (${candidate.headline})",
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

                // Role title
                Text("Role Title *", color = SkillspotTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = roleTitle,
                    onValueChange = { roleTitle = it },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SkillspotDarkSurface,
                        unfocusedContainerColor = SkillspotDarkSurface,
                        focusedBorderColor = SkillspotYellow,
                        unfocusedBorderColor = SkillspotOutline,
                        focusedTextColor = SkillspotTextPrimary,
                        unfocusedTextColor = SkillspotTextPrimary,
                        cursorColor = SkillspotYellow
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("recruit_role_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Compensation
                Text("Offered Stipend / Compensation *", color = SkillspotTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = compensation,
                    onValueChange = { compensation = it },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SkillspotDarkSurface,
                        unfocusedContainerColor = SkillspotDarkSurface,
                        focusedBorderColor = SkillspotYellow,
                        unfocusedBorderColor = SkillspotOutline,
                        focusedTextColor = SkillspotTextPrimary,
                        unfocusedTextColor = SkillspotTextPrimary,
                        cursorColor = SkillspotYellow
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("recruit_comp_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Personal Invitation Message
                Text("Recruitment Invitation Message *", color = SkillspotTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = inviteMessage,
                    onValueChange = { inviteMessage = it },
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
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("recruit_message_input")
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onSendOffer(roleTitle, compensation, inviteMessage) },
                    enabled = roleTitle.isNotBlank() && inviteMessage.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SkillspotYellow,
                        contentColor = SkillspotBlack
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("send_recruitment_offer_button")
                ) {
                    Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Send Direct Recruitment Offer", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}
