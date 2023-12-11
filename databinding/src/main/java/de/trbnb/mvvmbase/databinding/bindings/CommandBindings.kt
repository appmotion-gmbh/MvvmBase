package de.trbnb.mvvmbase.databinding.bindings

import android.view.View
import androidx.databinding.BindingAdapter
import de.trbnb.mvvmbase.databinding.commands.Command

/**
 * Binds the given [Command] as command that will be invoked when the View has been clicked.
 * This will also bind the [View.isEnabled] property to the [Command.isEnabled] property.
 */
@BindingAdapter("clickCommand")
public fun View.bindClickCommand(command: Command<Unit, *>?) {
    if (command == null) {
        setOnClickListener(null)
        return
    }
    bindEnabled(command)

    setOnClickListener {
        command.invokeSafely(Unit)
    }
}

/**
 * Binds the [View.isEnabled] property to the [Command.isEnabled] property of the given instances.
 */
public fun View.bindEnabled(command: Command<*, *>?) {
    command ?: return
    isEnabled = command.isEnabled

    command.addEnabledListenerForView { enabled ->
        post {
            isEnabled = enabled
        }
    }
}

/**
 * Binds the given [Command] as command that will be invoked when the View has been long-clicked.
 */
@BindingAdapter("longClickCommand")
public fun View.bindLongClickCommand(command: Command<Unit, *>?) {
    if (command == null) {
        setOnLongClickListener(null)
        return
    }

    setOnLongClickListener {
        if (command.isEnabled) {
            command.invoke(Unit) as? Boolean ?: true
        } else {
            false
        }
    }
}
