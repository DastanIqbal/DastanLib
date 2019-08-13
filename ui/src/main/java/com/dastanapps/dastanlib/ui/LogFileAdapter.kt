package com.dastanapps.dastanlib.ui

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dastanapps.dastanlib.DastanLibApp
import com.dastanapps.dastanlib.ui.databinding.LogItemBinding
import com.dastanapps.dastanlib.utils.CommonUtils
import com.dastanapps.dastanlib.utils.DeviceUtils
import com.dastanapps.dastanlib.utils.ViewUtils

class LogFileAdapter(private val fileItemList: ArrayList<FileItemB>) : androidx.recyclerview.widget.RecyclerView.Adapter<LogFileAdapter.LogVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogVH {
        return LogVH(LogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = fileItemList.size

    override fun onBindViewHolder(holder: LogVH, position: Int) {
        holder.bind(fileItemList[position])
    }


    inner class LogVH(val binding: LogItemBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        fun bind(fileItemB: FileItemB) {
            binding.logName.text = fileItemB.filename
            binding.root.setOnClickListener {
                //TODO: Implement LogViewer
                DastanLibApp.INSTANCE.supportEmail?.run {
                    CommonUtils.sendEmailAttachment(binding.root.context, DeviceUtils.getMinimalDeviceInfo(), fileItemB.path, this)
                } ?: ViewUtils.showToast(it.context, "No support email founds")
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
