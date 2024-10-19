package com.litovkin.notesapp.di

import androidx.lifecycle.ViewModel
import com.litovkin.notesapp.presentation.add_edit_note.AddEditNoteViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AddEditNoteViewModelModule {

    @IntoMap
    @ViewModelKey(AddEditNoteViewModel::class)
    @Binds
    fun bindAddEditViewModel(viewModel: AddEditNoteViewModel): ViewModel
}