package de.trbnb.mvvmbase.databinding.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.trbnb.mvvmbase.databinding.ViewModel

/**
 * Basic [ListAdapter] implementation for ViewModel lists.
 *
 * Uses referential equality for [DiffUtil.ItemCallback.areContentsTheSame]
 * and [ViewModel.equals] for [DiffUtil.ItemCallback.areItemsTheSame] by default.
 *
 * @param layoutId Layout resource ID of the item layout.
 */
public open class BindingListAdapter<VM : ViewModel, B : ViewDataBinding>(
    public val layoutId: Int,
    diffItemCallback: DiffUtil.ItemCallback<VM> = object : DiffUtil.ItemCallback<VM>() {
        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: VM, newItem: VM) = oldItem == newItem
        override fun areItemsTheSame(oldItem: VM, newItem: VM) = oldItem === newItem
    }
) : ListAdapter<VM, BindingViewHolder<B>>(diffItemCallback) {
    override fun onBindViewHolder(holder: BindingViewHolder<B>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<B> = BindingViewHolder<B>(
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutId, parent, false)
    )
}

/**
 * Binding adapter function to make use of [BindingListAdapter].
 */
@BindingAdapter("items", "itemLayout")
public fun RecyclerView.setItems(items: List<ViewModel>, itemLayout: Int) {
    @Suppress("UNCHECKED_CAST")
    (adapter as? BindingListAdapter<ViewModel, ViewDataBinding>
        ?: BindingListAdapter<ViewModel, ViewDataBinding>(itemLayout).also { this.adapter = it }).submitList(items)
}
