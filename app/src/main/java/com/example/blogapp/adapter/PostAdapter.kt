package com.example.blogapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.blogapp.R
import com.example.blogapp.databinding.PostRvSampleBinding
import com.example.blogapp.model.Post

class PostAdapter(
    val onClick: (currentItem: Post) -> Unit,
    private val uid: String?,
    val onUpdateLiked:(currentItem:Post)->Unit
) : RecyclerView.Adapter<PostAdapter.MyViewHolder>() {

    private val item=ArrayList<Post>()
    private lateinit var onDelete: (postID: String) -> Unit
    private lateinit var onUpdate: (currentItem: Post) -> Unit

    constructor(
        onClick: (currentItem: Post) -> Unit,
        uid: String?,
        onDelete: (postID: String) -> Unit,
        onUpdate:(currentItem:Post)->Unit,
        onUpdateLiked: (currentItem: Post) -> Unit
    ) : this( onClick, uid,onUpdateLiked) {
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
            liked.setOnClickListener {
                onUpdateLiked(currentItem)
            }
            val isLiked=currentItem.likedBy.contains(uid)
            if (isLiked){
                liked.setImageResource(R.drawable.baseline_favorite_24)
            }else{
                liked.setImageResource(R.drawable.outline_favorite_border_24)
            }
            likedCount.text=currentItem.likedBy.size.toString()
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItem(updateItem: List<Post>) {
        item.clear()
        item.addAll(updateItem)
        notifyDataSetChanged()
    }


    inner class MyViewHolder(val binding: PostRvSampleBinding) :
        RecyclerView.ViewHolder(binding.root)

}