package de.trbnb.mvvmbase.databinding.bindings

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * Maps a visibility-boolean to either [View.VISIBLE] for `true` or [View.GONE] for `false`.
 */
@BindingAdapter("android:visible")
public fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}
