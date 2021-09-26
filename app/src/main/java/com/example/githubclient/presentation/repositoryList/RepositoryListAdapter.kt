package com.example.githubclient.presentation.repositoryList

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubclient.R
import com.example.githubclient.databinding.ItemRepoInfoBinding
import com.example.githubclient.domain.enteties.apiResponse.UserRepoResponse

sealed class RecyclerItemsTypes {
    data class Repository(val data: UserRepoResponse) : RecyclerItemsTypes()
    object Progress : RecyclerItemsTypes()
}

abstract class AbstractCustomViewHolder<T : RecyclerItemsTypes>(view: View) :
    RecyclerView.ViewHolder(view) {
    abstract fun bind(dataHolder: T)
}

abstract class AbstractCustomViewHolderFabric<T : AbstractCustomViewHolder<*>> {
    abstract fun create(parent: ViewGroup): T
}

class RepositoryViewHolder(view: View) :
    AbstractCustomViewHolder<RecyclerItemsTypes.Repository>(view) {
    companion object Fabric : AbstractCustomViewHolderFabric<RepositoryViewHolder>() {
        override fun create(parent: ViewGroup) = RepositoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_repo_info, parent, false
            )
        )
    }

    var onItemClickListener: ((url: String) -> Unit)? = null

    override fun bind(dataHolder: RecyclerItemsTypes.Repository) {
        with(dataHolder.data) {
            with(ItemRepoInfoBinding.bind(itemView)) {
                tvName.text = name

                with(itemView.context) {
                    tvCreated.text = getString(R.string.created, created)
                    tvPushed.text = getString(R.string.pushed, pushed)
                    tvUpdated.text = getString(R.string.updated, updated)
                }

                if (language == null) {
                    tvLanguage.visibility = GONE
                } else {
                    tvLanguage.text = language
                }

                btnGoToRepo.setOnClickListener {
                    onItemClickListener?.invoke(url)
                }
            }
        }
    }
}

class LoadViewHolder(view: View) :
    AbstractCustomViewHolder<RecyclerItemsTypes.Progress>(view) {
    companion object Fabric : AbstractCustomViewHolderFabric<LoadViewHolder>() {
        override fun create(parent: ViewGroup): LoadViewHolder {
            return LoadViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_repo_load, parent, false)
            )
        }
    }

    override fun bind(dataHolder: RecyclerItemsTypes.Progress) { }
}

class RepositoryListAdapter : RecyclerView.Adapter<AbstractCustomViewHolder<*>>() {
    companion object {
        const val REPO_ITEM = 1
        const val PROGRESS_ITEM = 2
    }

    private var items: MutableList<RecyclerItemsTypes> = mutableListOf()

    var onItemClickListener: ((url: String) -> Unit)? = null
    var onRequestItemsListener: (() -> Unit)? = null

    var isLoad = true
        set(value) {
            if (value) addProgress()
            else removeProgress()
            field = value
        }

    fun addItems(newItems: List<UserRepoResponse>) {
        if (isLoad) removeProgress()
        items.lastIndex.also {
            items.addAll(newItems.map(RecyclerItemsTypes::Repository))
            notifyItemRangeChanged(it, newItems.size)
        }
        if (isLoad) addProgress()
    }

    override fun getItemViewType(position: Int): Int {
        return when(items[position]) {
            RecyclerItemsTypes.Progress -> PROGRESS_ITEM
            is RecyclerItemsTypes.Repository -> REPO_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractCustomViewHolder<*> {
        return when(viewType) {
            REPO_ITEM -> RepositoryViewHolder.create(parent)
            else -> LoadViewHolder.create(parent)
        }
    }

    override fun onBindViewHolder(holder: AbstractCustomViewHolder<*>, position: Int) {
        when(holder.itemViewType) {
            REPO_ITEM -> with(holder as RepositoryViewHolder) {
                bind(items[position] as RecyclerItemsTypes.Repository)
                onItemClickListener = this@RepositoryListAdapter.onItemClickListener
            }

            PROGRESS_ITEM -> onRequestItemsListener?.invoke()
        }
    }

    override fun getItemCount(): Int = items.size;

    private fun removeProgress() {
        if (items.isNotEmpty() && items.last() is RecyclerItemsTypes.Progress) {
            items.removeLast()
            notifyItemRemoved(items.lastIndex + 1)
        }
    }

    private fun addProgress() {
        if (items.isEmpty() || items.last() !is RecyclerItemsTypes.Progress) {
            items.add(RecyclerItemsTypes.Progress)
            notifyItemInserted(items.lastIndex)
        }
    }
}