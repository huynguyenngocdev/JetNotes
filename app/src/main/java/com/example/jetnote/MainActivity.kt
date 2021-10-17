package com.example.jetnote

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Surface
import com.example.jetnote.routing.JetNotesRouter
import com.example.jetnote.routing.Screen
import com.example.jetnote.theme.JetNotesTheme
import com.example.jetnote.ui.screens.NotesScreen
import com.example.jetnote.ui.screens.SaveNoteScreen
import com.example.jetnote.ui.screens.TrashScreen
import com.example.jetnote.viewmodel.MainViewModel
import com.example.jetnote.viewmodel.MainViewModelFactory

/**
 * Main activity for the app.
 */
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels(factoryProducer = {
        MainViewModelFactory(
            this,
            (application as JetNotesApplication).dependencyInjector.repository
        )
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JetNotesTheme {
                Surface {
                    when (JetNotesRouter.currentScreen) {
                        is Screen.Notes -> NotesScreen(viewModel)
                        is Screen.Trash -> TrashScreen(viewModel)
                        is Screen.SaveNote -> SaveNoteScreen(viewModel)
                    }
                }
            }
        }
    }
}
