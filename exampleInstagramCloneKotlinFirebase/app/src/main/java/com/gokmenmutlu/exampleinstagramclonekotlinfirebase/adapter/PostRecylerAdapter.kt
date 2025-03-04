package com.gokmenmutlu.exampleinstagramclonekotlinfirebase.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.R
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.databinding.RecyclerPostRowBinding
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.model.Post
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.utils.TimeUtils.formatTimeAgo

class PostRecylerAdapter(private val postList: List<Post>) : RecyclerView.Adapter<PostRecylerAdapter.AdapterViewHolder>() {

    // Ana Sayfa
    class AdapterViewHolder( val binding: RecyclerPostRowBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val binding = RecyclerPostRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return AdapterViewHolder(binding)
    }

    override fun getItemCount(): Int = postList.size


    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {

        val post = postList[position]

        holder.binding.txtUserEmail.text = post.userEmail
        holder.binding.txtTitle.text = post.title
        holder.binding.txtComment.text = post.comment
        if (post.comment.isNullOrEmpty()) {
            holder.binding.txtComment.visibility = View.GONE }

        post.date?.let {
            holder.binding.txtDate.text = formatTimeAgo(it) // <- Ne zaman paylaşıldığı
        }


        Glide.with(holder.binding.imgPost.context)
            .load(postList[position].downloadImageUrl)
            .placeholder(R.drawable.green_on_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.binding.imgPost)

    }
}