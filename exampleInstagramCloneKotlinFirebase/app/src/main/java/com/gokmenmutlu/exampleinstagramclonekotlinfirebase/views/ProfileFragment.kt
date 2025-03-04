package com.gokmenmutlu.exampleinstagramclonekotlinfirebase.views

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.R
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.activities.LoginActivity
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.adapter.ProfilePostAdapter
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.databinding.FragmentProfileBinding
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var firestore: FirebaseFirestore
    private var postList = mutableListOf<Post>()
    private lateinit var postAdapter: ProfilePostAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = FirebaseFirestore.getInstance()

        setupRecyclerView()
        getUserPosts()
        setupSettingsMenu()
    }


    private fun setupRecyclerView() {
        postAdapter = ProfilePostAdapter(postList, findNavController())

        binding.recyclerViewProfile.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewProfile.adapter = postAdapter
    }

    private fun getUserPosts() {
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

        if (currentUserEmail == null) {
            Toast.makeText(context, "Kullanıcı bilgisi alınamadı", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("Posts")
            .whereEqualTo("userEmail", currentUserEmail)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    println("Firestore Error: ${error.localizedMessage}")
                    Toast.makeText(context, "Hata: ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) { // <- chatGPT den yardım alındı.
                    println("Firestore'dan kullanıcı postları geldi!")

                    val tempList = mutableListOf<Post>() // Geçici liste
                    for (document in snapshot.documents) {
                        val post = document.toObject(Post::class.java)
                        post?.postId = document.id

                        post?.let { tempList.add(it)
                        }
                    }
                    if (tempList.isNotEmpty()) {
                        if(_binding!= null) {
                            binding.profileUsername.text = tempList[0].userEmail
                        } // silme işleminden sonra profile'e geri dönüldüğünde hata vermemesi icin null kontrolü yapıyoruz.
                    }
                    println("Güncellenen Post Listesi: $tempList") //Test
                    postAdapter.updatePosts(tempList) // Adapter'ı yeni listeyle güncelle
                } else {
                    println("Bu kullanıcıya ait post bulunamadı.")
                }
            }
    }

    private fun setupSettingsMenu() {
        binding.imageViewSettings.setOnClickListener { view ->

            val popupMenu = PopupMenu(requireContext(), view)

            popupMenu.menuInflater.inflate(R.menu.profile_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.ProfileLogout -> {
                        logoutUser()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }

    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()

        // Firestore listener'ları temizle
        firestore.terminate()

        // Login e geri dön.
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

