package de.trbnb.mvvmbase.databinding.savedstate

import androidx.lifecycle.SavedStateHandle
import de.trbnb.mvvmbase.databinding.BaseViewModel
import de.trbnb.mvvmbase.savedstate.StateSavingViewModel

/**
 * Base implementation for [StateSavingViewModel].
 * Receives the [SavedStateHandle] via construction parameter.
 */
public abstract class BaseStateSavingViewModel(
    final override val savedStateHandle: SavedStateHandle
) : BaseViewModel(), StateSavingViewModel
