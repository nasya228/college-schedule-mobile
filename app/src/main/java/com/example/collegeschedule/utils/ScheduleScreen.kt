package com.example.collegeschedule.ui.schedule

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.local.FavoritesDataStore
import com.example.collegeschedule.data.network.RetrofitInstance
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(modifier: Modifier = Modifier) {
    var groups by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedGroup by remember { mutableStateOf("") }
    var schedule by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }

    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val favoritesDataStore = remember { FavoritesDataStore(context) }
    val favorites by favoritesDataStore.favoritesFlow.collectAsState(initial = emptySet())

    val isFavorite = favorites.contains(selectedGroup)


    LaunchedEffect(Unit) {
        try {
            val response = RetrofitInstance.api.getAllGroups()
            groups = response
            if (response.isNotEmpty() && selectedGroup.isEmpty()) {
                selectedGroup = response[0]
                searchText = response[0]
            }
        } catch (e: Exception) {
            Log.e("SCHEDULE_DEBUG", "Ошибка загрузки списка групп: ${e.message}")
        }
    }


    LaunchedEffect(selectedGroup) {
        if (selectedGroup.isBlank()) return@LaunchedEffect

        loading = true
        error = null
        val start = "2026-03-17"
        val end = "2026-03-22"

        try {
            val response = RetrofitInstance.api.getSchedule(selectedGroup, start, end)
            schedule = response
            if (response.all { it.lessons.isEmpty() }) {
                error = "Для группы $selectedGroup занятий нет"
            }
        } catch (e: Exception) {
            error = "Ошибка загрузки: сервер недоступен"
        } finally {
            loading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Расписание") },
                actions = {
                    if (selectedGroup.isNotBlank()) {
                        IconButton(onClick = {
                            scope.launch {
                                if (isFavorite) favoritesDataStore.removeFavorite(selectedGroup)
                                else favoritesDataStore.addFavorite(selectedGroup)
                            }
                        }) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = null,
                                tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = modifier.fillMaxSize().padding(innerPadding).padding(16.dp)) {


            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it; expanded = true },
                    label = { Text("Введите или выберите группу") },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )

                val filteringOptions = groups.filter { it.contains(searchText, ignoreCase = true) }

                if (filteringOptions.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        filteringOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedGroup = option
                                    searchText = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (loading) {
                Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
            } else if (error != null) {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text(text = error!!, style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(schedule) { day ->
                        if (day.lessons.isNotEmpty()) {
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(Modifier.padding(16.dp)) {
                                    Text(
                                        text = "${day.weekday}, ${day.lessonDate.substringBefore("T")}",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    HorizontalDivider(Modifier.padding(vertical = 8.dp))

                                    day.lessons.forEach { lesson ->

                                        val details = lesson.groupParts.FULL
                                            ?: lesson.groupParts.SUB1
                                            ?: lesson.groupParts.SUB2

                                        ListItem(
                                            headlineContent = {
                                                Text("${lesson.lessonNumber}. ${details?.subject ?: "Самоподготовка"}")
                                            },
                                            supportingContent = {
                                                Text("${lesson.time}${if (lesson.groupParts.FULL == null && details != null) " (подгруппа)" else ""}")
                                            },
                                            trailingContent = {
                                                Text("каб. ${details?.classroom ?: "-"}")
                                            }
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
}