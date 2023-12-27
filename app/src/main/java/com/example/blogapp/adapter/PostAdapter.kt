package com.example.blogapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.blogapp.databinding.PostRvSampleBinding
import com.example.blogapp.model.Post

class PostAdapter(
    private val item: List<Post>,
    val onClick: (currentItem: Post) -> Unit,
    private val uid: String?,
) : RecyclerView.Adapter<PostAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            PostRvSampleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = item[position]

        holder.binding.apply {
            postAuthor.text = currentItem.authorName
            postTitle.text = currentItem.title
            description.text = currentItem.description
            postTime.text = currentItem.date
            postReadButton.setOnClickListener {
                onClick(currentItem)
            }
            if (currentItem.authorId == uid) {
                delete.visibility = View.VISIBLE
            }else{
                delete.setOnClickListener {
                    TODO("Yet to implement")
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

    inner class MyViewHolder(val binding: PostRvSampleBinding) :
        RecyclerView.ViewHolder(binding.root)

}