package com.gokmenmutlu.exampleinstagramclonekotlinfirebase.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.R
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.databinding.ProfilePostAdapterBinding
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.model.Post
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.utils.TimeUtils.formatTimeAgo
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class ProfilePostAdapter(
    private var postList: MutableList<Post>,
    private val navController: NavController
) : RecyclerView.Adapter<ProfilePostAdapter.ViewHolder>() {

    class ViewHolder(val binding: ProfilePostAdapterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProfilePostAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = postList[position]

        holder.binding.postUserMail.text = post.userEmail
        holder.binding.postDescription.text = post.title
        post.date?.let {
            holder.binding.postTime.text = formatTimeAgo(it) // <- Ne zaman paylaşıldıgı işlemi.
        }

        Glide.with(holder.itemView.context)
            .load(post.downloadImageUrl)
            .placeholder(R.drawable.green_on_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.binding.imageViewPost)

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("postId", post.postId) // for Delete.
            bundle.putString("imageUrl", post.downloadImageUrl)

            navController.navigate(R.id.action_profileFragment_to_postDetailFragment, bundle)
        }
    }

    override fun getItemCount(): Int = postList.size


    fun updatePosts(newList: List<Post>) {

        postList.clear()
        postList.addAll(newList)
        notifyDataSetChanged()
    }

}
