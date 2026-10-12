package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DirectMessage
import com.example.data.model.UserRole
import com.example.ui.theme.*

@Composable
fun MessagesScreen(
    messages: List<DirectMessage>,
    currentRole: UserRole,
    activeConversationId: String,
    activeRecipientName: String,
    onSendMessage: (conversationId: String, text: String, recipientName: String, jobTitle: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var textInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Filter messages for active conversation or show general thread
    val threadMessages = messages.filter {
        it.conversationId == activeConversationId || activeConversationId.isBlank()
    }.ifEmpty { messages }

    val quickReplies = listOf(
        "I'm available for a technical interview this Thursday!",
        "Feel free to check out my live GitHub repo attached to my portfolio.",
        "Could you share more about the mentor structure for this internship?",
        "Thank you! Looking forward to speaking with the engineering team."
    )

    LaunchedEffect(threadMessages.size) {
        if (threadMessages.isNotEmpty()) {
            listState.animateScrollToItem(threadMessages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SkillspotBlack)
    ) {
        // Conversation Header with direct connection context
        Surface(
            color = SkillspotDarkCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, SkillspotOutline),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(SkillspotYellow)
                        .border(1.dp, SkillspotBlack, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Business,
                        contentDescription = null,
                        tint = SkillspotBlack,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = activeRecipientName,
                            color = SkillspotTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Circle,
                            contentDescription = "Online",
                            tint = SkillspotGreen,
                            modifier = Modifier.size(8.dp)
                        )
                    }
                    Text(
                        text = "Direct Recruiter & Candidate Connection • Active Now",
                        color = SkillspotYellow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Messages list
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(threadMessages, key = { it.id }) { msg ->
                val isMe = (currentRole == msg.senderRole)

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
                ) {
                    // Sender info
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isMe) "You" else msg.senderName,
                            color = SkillspotTextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = msg.timestamp,
                            color = SkillspotTextMuted,
                            fontSize = 10.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Message bubble
                    Surface(
                        color = if (isMe) SkillspotYellow else SkillspotDarkCard,
                        shape = RoundedCornerShape(
                            topStart = 14.dp,
                            topEnd = 14.dp,
                            bottomStart = if (isMe) 14.dp else 2.dp,
                            bottomEnd = if (isMe) 2.dp else 14.dp
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isMe) SkillspotYellow else SkillspotOutline
                        ),
                        modifier = Modifier.widthIn(max = 280.dp)
                    ) {
                        Text(
                            text = msg.messageText,
                            color = if (isMe) SkillspotBlack else SkillspotTextPrimary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }

        // Quick replies row
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quickReplies) { reply ->
                Surface(
                    color = SkillspotDarkCard,
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SkillspotOutline),
                    modifier = Modifier.clickable {
                        onSendMessage(activeConversationId, reply, activeRecipientName, "General")
                    }
                ) {
                    Text(
                        text = reply,
                        color = SkillspotTextSecondary,
                        fontSize = 11.sp,
                        maxLines = 1,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Bottom text input row
        Surface(
            color = SkillspotDarkCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, SkillspotOutline),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = {
                        Text(
                            text = "Direct message to $activeRecipientName...",
                            color = SkillspotTextMuted,
                            fontSize = 13.sp
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SkillspotDarkSurface,
                        unfocusedContainerColor = SkillspotDarkSurface,
                        focusedBorderColor = SkillspotYellow,
                        unfocusedBorderColor = SkillspotOutline,
                        focusedTextColor = SkillspotTextPrimary,
                        unfocusedTextColor = SkillspotTextPrimary,
                        cursorColor = SkillspotYellow
                    ),
                    shape = RoundedCornerShape(20.dp),
                    maxLines = 3,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_message_input")
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (textInput.isNotBlank()) {
                            onSendMessage(activeConversationId, textInput, activeRecipientName, "General")
                            textInput = ""
                        }
                    },
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(if (textInput.isNotBlank()) SkillspotYellow else SkillspotDarkSurface)
                        .testTag("send_chat_message_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send message",
                        tint = if (textInput.isNotBlank()) SkillspotBlack else SkillspotTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
