package com.litovkin.notesapp.di

import com.litovkin.notesapp.presentation.ViewModelFactory
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(
    modules = [
        AddEditNoteViewModelModule::class
    ]
)
interface AddEditNoteComponent {

    fun getViewModelFactory(): ViewModelFactory

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance noteId: Int
        ): AddEditNoteComponent
    }
}