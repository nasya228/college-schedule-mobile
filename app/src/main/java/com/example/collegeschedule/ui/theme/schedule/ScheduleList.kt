package com.example.collegeschedule.ui.theme.schedule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.dto.LessonGroupPart

@Composable
fun ScheduleList(
    data: List<ScheduleByDateDto>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(data) { day ->

            Text(
                text = "${day.lessonDate} (${day.weekday})",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp, 8.dp, 16.dp, 4.dp)
            )

            if (day.lessons.isEmpty()) {
                Text(
                    text = "Нет занятий",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                )
            } else {
                day.lessons.forEach { lesson ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
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

                            lesson.groupParts.forEach { (part, info) ->
                                if (info != null) {
                                    val partLabel = when (part) {
                                        LessonGroupPart.FULL -> ""
                                        LessonGroupPart.SUB1 -> " (подгруппа 1)"
                                        LessonGroupPart.SUB2 -> " (подгруппа 2)"
                                    }
                                    Text(
                                        text = "${info.subject}$partLabel",
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                    Text(
                                        text = info.teacher,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "${info.building}, ауд. ${info.classroom}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}