package com.example.collegeschedule.data.dto

data class ScheduleByDateDto(
    val lessonDate: String, // Формат: 2026-01-12
    val weekday: String,
    val lessons: List<LessonDto>
)