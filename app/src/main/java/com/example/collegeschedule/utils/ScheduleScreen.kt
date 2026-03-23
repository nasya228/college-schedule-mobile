package com.example.collegeschedule.ui.schedule

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.network.RetrofitInstance
import com.example.collegeschedule.ui.theme.schedule.ScheduleList
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleScreen(modifier: Modifier = Modifier) {
    var schedule by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {

        val startDate = LocalDate.of(2026, 3, 17)
        val endDate = LocalDate.of(2026, 3, 22)
        val formatter = DateTimeFormatter.ISO_DATE

        val start = startDate.format(formatter)
        val end = endDate.format(formatter)

        Log.d("ScheduleScreen", "Запрос: groupName=ИС-12, start=$start, end=$end")

        try {
            schedule = RetrofitInstance.api.getSchedule("ИС-12", start, end)
            Log.d("ScheduleScreen", "Успешно! Получено ${schedule.size} дней")
            if (schedule.isNotEmpty()) {
                Log.d("ScheduleScreen", "Первый день: ${schedule[0].lessonDate}")
            }
        } catch (e: Exception) {
            error = e.message
            Log.e("ScheduleScreen", "Ошибка: ${e.message}", e)
        } finally {
            loading = false
        }
    }

    when {
        loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        error != null -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ошибка: $error")
            }
        }
        else -> {
            ScheduleList(data = schedule, modifier = modifier)
        }
    }
}