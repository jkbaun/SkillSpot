package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        JobEntity::class,
        CandidateEntity::class,
        PortfolioEntity::class,
        ApplicationEntity::class,
        WorkshopEntity::class,
        MessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SkillspotDatabase : RoomDatabase() {
    abstract fun dao(): SkillspotDao

    companion object {
        @Volatile
        private var INSTANCE: SkillspotDatabase? = null

        fun getDatabase(context: Context): SkillspotDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SkillspotDatabase::class.java,
                    "skillspot_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
