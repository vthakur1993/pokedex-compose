package com.example.pokedex.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1,2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
                CREATE TABLE IF NOT EXISTS PokemonInfo (
                id INTEGER NOT NULL, 
                nameField TEXT PRIMARY KEY NOT NULL, 
                height INTEGER NOT NULL, 
                weight INTEGER NOT NULL, 
                exp INTEGER NOT NULL, 
                types TEXT NOT NULL, 
                stats TEXT NOT NULL
                );
            """.trimIndent()
        )
    }
}