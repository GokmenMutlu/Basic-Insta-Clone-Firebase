package com.gokmenmutlu.exampleinstagramclonekotlinfirebase.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.R
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.databinding.FragmentPostDetailBinding
import com.google.firebase.firestore.FirebaseFirestore


class PostDetailFragment : Fragment() {

    private var _binding : FragmentPostDetailBinding ?= null
    private val binding get() = _binding!!
    private var postId: String? = null
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentPostDetailBinding.inflate(inflater,container,false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = FirebaseFirestore.getInstance()

        arguments?.let { bundle ->
            postId = bundle.getString("postId")
            val imageUrl = bundle.getString("imageUrl")
           loadImage(imageUrl)
        }

        binding.btnDeletePost.setOnClickListener {
            deletePost()
        }

    }

    private fun loadImage(imageUrl: String?) {
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.green_on_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(binding.imageViewPostDetail)
    }

    private fun deletePost() {
        if (postId.isNullOrEmpty()) {
            Toast.makeText(context, "Post bilgisi alınamadı!", Toast.LENGTH_SHORT).show()
            return
        }
        firestore.collection("Posts").document(postId!!)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Post başarıyla silindi!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_postDetailFragment_to_profileFragment) //  Bir önceki ekrana dön
            }.addOnFailureListener {
                Toast.makeText(context, "Silme işlemi başarısız: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                println(it.localizedMessage)
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}