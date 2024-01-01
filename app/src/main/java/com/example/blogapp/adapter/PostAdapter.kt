package com.example.blogapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.blogapp.databinding.PostRvSampleBinding
import com.example.blogapp.model.Post

class PostAdapter(
    private val item: List<Post>,
    val onClick: (currentItem: Post) -> Unit,
    private val uid: String?,
) : RecyclerView.Adapter<PostAdapter.MyViewHolder>() {

    private lateinit var onDelete: (postID: String) -> Unit
    private lateinit var onUpdate: (currentItem: Post) -> Unit

    constructor(
        item: List<Post>,
        onClick: (currentItem: Post) -> Unit,
        uid: String?,
        onDelete: (postID: String) -> Unit,
        onUpdate:(currentItem:Post)->Unit
    ) : this(item, onClick, uid) {
        this.onDelete = onDelete
        this.onUpdate=onUpdate
    }

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
                edit.visibility=View.VISIBLE
            }
            delete.setOnClickListener {
                onDelete(currentItem.postId)
            }
            edit.setOnClickListener {
                onUpdate(currentItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

    inner class MyViewHolder(val binding: PostRvSampleBinding) :
        RecyclerView.ViewHolder(binding.root)

}