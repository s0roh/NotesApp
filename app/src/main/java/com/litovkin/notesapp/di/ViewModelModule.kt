package com.litovkin.notesapp.di

import androidx.lifecycle.ViewModel
import com.litovkin.notesapp.presentation.notes.NotesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NotesViewModel::class)
    fun bindNoteViewModel(viewModel: NotesViewModel): ViewModel
}