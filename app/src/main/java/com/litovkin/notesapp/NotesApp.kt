package com.litovkin.notesapp

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.litovkin.notesapp.di.ApplicationComponent
import com.litovkin.notesapp.di.DaggerApplicationComponent

class NotesApp: Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}

@Composable
fun getApplicationComponent(): ApplicationComponent {
    return (LocalContext.current.applicationContext as NotesApp).component
}