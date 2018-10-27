package com.dastanapps.dastanlib.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dastanapps.dastanlib.databinding.LogItemBinding
import com.dastanapps.dastanlib.utils.CommonUtils

class LogFileAdapter(private val fileItemList: ArrayList<FileItemB>) : RecyclerView.Adapter<LogFileAdapter.LogVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogVH {
        return LogVH(LogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = fileItemList.size

    override fun onBindViewHolder(holder: LogVH, position: Int) {
        holder.bind(fileItemList[position])
    }

    private var bodyContent: String = ""

    fun setBodyContent(stringExtra: String) {
        bodyContent = stringExtra
    }

    inner class LogVH(val binding: LogItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(fileItemB: FileItemB) {
            binding.logName.text = fileItemB.filename
            binding.root.setOnClickListener {
                CommonUtils.sendEmailAttachment(binding.root.context, bodyContent, fileItemB.path, "myemail@domain.com")
            }
        }
    }
}

data class FileItemB(var filename: String, var path: String)

class FilterComparator : Comparator<FileItemB> {
    override fun compare(first: FileItemB, second: FileItemB): Int {
        return first.filename.compareTo(second.filename)
    }
}
