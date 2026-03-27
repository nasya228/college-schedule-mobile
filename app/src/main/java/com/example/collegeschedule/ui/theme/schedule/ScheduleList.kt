package com.example.collegeschedule.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.dto.LessonGroupPart
import com.example.collegeschedule.data.dto.LessonDetailsDto

@Composable
fun ScheduleList(data: List<ScheduleByDateDto>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(data) { day ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "${day.lessonDate.split("T")[0]} (${day.weekday})",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    day.lessons.forEach { lesson ->

                        val activeParts = mutableListOf<Pair<String, LessonDetailsDto>>()
                        lesson.groupParts.FULL?.let { activeParts.add("" to it) }
                        lesson.groupParts.SUB1?.let { activeParts.add(" (подгруппа 1)" to it) }
                        lesson.groupParts.SUB2?.let { activeParts.add(" (подгруппа 2)" to it) }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = when (lesson.lessonNumber) {
                                    1 -> Color(0xFFE3F2FD)
                                    2 -> Color(0xFFE8F5E9)
                                    3 -> Color(0xFFFFF3E0)
                                    else -> Color(0xFFF3E5F5)
                                }
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "${lesson.lessonNumber} пара • ${lesson.time}",
                                    style = MaterialTheme.typography.titleSmall
                                )

                                if (activeParts.isEmpty()) {
                                    Text("Окно / Самоподготовка", style = MaterialTheme.typography.bodyMedium)
                                } else {
                                    activeParts.forEach { (label, info) ->
                                        Text(
                                            text = "${info.subject ?: "Предмет не указан"}$label",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text(
                                            text = info.teacher ?: "Преподаватель не указан",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = "${info.building ?: ""}, ауд. ${info.classroom ?: "-"}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        if (activeParts.size > 1 && label != activeParts.last().first) {
                                            Divider(modifier = Modifier.padding(vertical = 4.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}