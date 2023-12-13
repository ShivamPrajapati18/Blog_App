package com.example.blogapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.blogapp.databinding.PostRvSampleBinding
import com.example.blogapp.model.Post

class PostAdapter(val item:List<Post>): RecyclerView.Adapter<PostAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding=PostRvSampleBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem=item[position]
        holder.binding.postAuthor.text="ABCD"
        holder.binding.postTitle.text=currentItem.title
        holder.binding.description.text=currentItem.description
    }

    override fun getItemCount(): Int {
        return item.size
    }

    inner class MyViewHolder(val binding:PostRvSampleBinding): RecyclerView.ViewHolder(binding.root)

}