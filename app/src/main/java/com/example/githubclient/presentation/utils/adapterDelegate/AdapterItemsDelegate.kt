package com.example.githubclient.presentation.utils.adapterDelegate

import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KProperty

class AdapterItemsDelegate<T : Any>(
    initValue: T,
    private val adapter: RecyclerView.Adapter<*>
) {
    private var data: T = initValue

    operator fun getValue(thisRef: Any?, property: KProperty<*>) = data

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        data = value
        adapter.notifyDataSetChanged()
    }
}

fun <T: Any> RecyclerView.Adapter<*>.adapterItems(initValue: T) = AdapterItemsDelegate(initValue, this)