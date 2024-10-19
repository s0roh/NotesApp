package com.litovkin.notesapp.di

import android.content.Context
import com.litovkin.notesapp.data.data_source.NoteDatabase
import com.litovkin.notesapp.data.data_source.NoteDatabaseDao
import com.litovkin.notesapp.data.repository.NoteRepositoryImpl
import com.litovkin.notesapp.domain.repository.NoteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides


@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindRepository(impl: NoteRepositoryImpl): NoteRepository

    companion object {

        @Provides
        @ApplicationScope
        fun provideNoteDatabase(context: Context): NoteDatabase {
            return NoteDatabase.getInstance(context)
        }

        @Provides
        @ApplicationScope
        fun provideNoteDatabaseDao(database: NoteDatabase): NoteDatabaseDao {
            return database.noteDatabaseDao()
        }
    }
}