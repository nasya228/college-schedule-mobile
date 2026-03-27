package com.example.collegeschedule.data.dto

data class LessonDto(
    val lessonNumber: Int,
    val time: String,
    val groupParts: GroupPartsDto
) {

    fun getActiveParts(): Map<LessonGroupPart, LessonDetailsDto> {
        val map = mutableMapOf<LessonGroupPart, LessonDetailsDto>()
        groupParts.FULL?.let { map[LessonGroupPart.FULL] = it }
        groupParts.SUB1?.let { map[LessonGroupPart.SUB1] = it }
        groupParts.SUB2?.let { map[LessonGroupPart.SUB2] = it }
        return map
    }
}

data class GroupPartsDto(
    val FULL: LessonDetailsDto?,
    val SUB1: LessonDetailsDto?,
    val SUB2: LessonDetailsDto?
)

data class LessonDetailsDto(
    val subject: String?,
    val teacher: String?,
    val classroom: String?,
    val building: String?,
    val address: String?
)