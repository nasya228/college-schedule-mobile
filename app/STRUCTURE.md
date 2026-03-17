# Mobile App Structure

## Пакеты

### data.api
- Интерфейсы Retrofit для API запросов

### data.dto
- Data Transfer Objects (соответствуют API сервера)

### data.repository
- Репозитории для работы с данными

### ui.schedule
- Экран расписания и ViewModel

### ui.components
- Переиспользуемые UI компоненты

### utils
- Вспомогательные утилиты (DateUtils и др.)

## Сетевые запросы
- Базовый URL: http://10.0.2.2:5268/
- Retrofit + Gson конвертер

## Локальное хранение
- DataStore для избранных групп
