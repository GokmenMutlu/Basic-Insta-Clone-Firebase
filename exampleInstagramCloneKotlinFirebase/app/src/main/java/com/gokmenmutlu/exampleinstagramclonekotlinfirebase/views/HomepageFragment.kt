package com.gokmenmutlu.exampleinstagramclonekotlinfirebase.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.R
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.adapter.PostRecylerAdapter
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.databinding.FragmentHomepageBinding
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.model.Post
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject


class HomepageFragment : Fragment() {

    private var _binding: FragmentHomepageBinding? = null
    private val binding get() = _binding!!

    private lateinit var postAdapter: PostRecylerAdapter
    private var postList = mutableListOf<Post>()
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomepageBinding.inflate(inflater, container, false)
        val view = binding.root

        firestore = FirebaseFirestore.getInstance()

        setupRecyclerView()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // snapshotListener ile tüm verileri dinliyoruz
        getPosts()

        binding.recyclerViewHome.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // LinearLayoutManager'ı kullanarak son elemanı kontrol et
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                // Eğer son elemana ulaşıldıysa
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                    // Hepsini gördünüz ImageView'ini görünür yap
                    binding.allDataLoadedImage.visibility = View.VISIBLE
                    binding.textView.visibility = View.VISIBLE
                } else {
                    // Değilse gizle
                    binding.allDataLoadedImage.visibility = View.GONE
                    binding.textView.visibility = View.GONE
                }
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        postAdapter = PostRecylerAdapter(postList)
        binding.recyclerViewHome.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewHome.adapter = postAdapter
    }

    // snapshotListener ile tüm veriyi alıyoruz
    private fun getPosts() {
        firestore.collection("Posts")
            .orderBy("date", Query.Direction.DESCENDING) // En son paylaşılan postları al
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    Toast.makeText(context, error.localizedMessage, Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    // Tüm veriyi sürekli olarak alıyoruz ve RecyclerView'ı güncelliyoruz
                    postList.clear() // Mevcut listeyi temizleyin
                    val newPosts = snapshot.toObjects(Post::class.java)
                    postList.addAll(newPosts) // Yeni verileri ekle
                    postAdapter.notifyDataSetChanged() // RecyclerView'ı güncelle
                }
            }
    }
}





