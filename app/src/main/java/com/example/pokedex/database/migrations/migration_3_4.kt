package com.example.pokedex.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_3_4 = object : Migration(3,4) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE PokemonAsset DROP COLUMN trainerInfo")
        db.execSQL("ALTER TABLE PokemonInfo DROP COLUMN trainerInfo")

        db.execSQL(
            """
                db.execSQL("ALTER TABLE PokemonAsset ADD COLUMN isFavourite Boolean NOT NULL DEFAULT false")
            """.trimIndent()
        )
        db.execSQL(
            """
                db.execSQL("ALTER TABLE PokemonInfo ADD COLUMN isFavourite Boolean NOT NULL DEFAULT false")
            """.trimIndent()
        )
    }

}