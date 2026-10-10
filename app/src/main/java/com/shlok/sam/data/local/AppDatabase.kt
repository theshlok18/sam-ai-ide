package com.shlok.sam.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.shlok.sam.data.model.AgentTask
import com.shlok.sam.data.model.ChatMessage
import com.shlok.sam.data.model.Conversation
import com.shlok.sam.data.model.MemoryCategory
import com.shlok.sam.data.model.MemoryItem
import com.shlok.sam.data.model.MessageRole
import com.shlok.sam.data.model.Project
import com.shlok.sam.data.model.ProjectType

class Converters {
    @TypeConverter
    fun fromMessageRole(role: MessageRole): String = role.name

    @TypeConverter
    fun toMessageRole(value: String): MessageRole = MessageRole.valueOf(value)

    @TypeConverter
    fun fromProjectType(type: ProjectType): String = type.name

    @TypeConverter
    fun toProjectType(value: String): ProjectType = ProjectType.valueOf(value)

    @TypeConverter
    fun fromMemoryCategory(category: MemoryCategory): String = category.name

    @TypeConverter
    fun toMemoryCategory(value: String): MemoryCategory = MemoryCategory.valueOf(value)
}

@Database(
    entities = [Conversation::class, ChatMessage::class, Project::class, MemoryItem::class, AgentTask::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun projectDao(): ProjectDao
    abstract fun memoryDao(): MemoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sam_autonomous_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
