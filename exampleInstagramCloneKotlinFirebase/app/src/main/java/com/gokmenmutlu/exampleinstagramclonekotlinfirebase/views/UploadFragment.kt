package com.gokmenmutlu.exampleinstagramclonekotlinfirebase.views

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.R
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.databinding.FragmentUploadBinding
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.viewModels.MainViewModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import java.util.UUID


class UploadFragment : Fragment() {

    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!

    private  val viewModel: MainViewModel by activityViewModels()

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore // db
    private lateinit var firebaseStorage: FirebaseStorage // resimlerin depolanacagı alan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUploadBinding.inflate(inflater,container,false)
        val view = binding.root

        auth = Firebase.auth
        firestore = Firebase.firestore
        firebaseStorage = Firebase.storage

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedImageUri.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                binding.imageView.setImageURI(it)
            }
        }

        binding.ShareButton.setOnClickListener {
            val imageUri = viewModel.selectedImageUri.value
            val title = binding.titleEditTxt.text.toString()
            val comments = binding.commentEditTxt.text.toString()

            imageUri?.let {
                //firebase Save
                uploadImage(it,title,comments)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun uploadImage(imageUri: Uri, title: String, comments: String) {
        Toast.makeText(requireContext(),"Loading Please Wait..", Toast.LENGTH_LONG).show()
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"
        // ChatGpt den yardim alındı bu kısımda ->
        val reference = firebaseStorage.reference
        val imageReference = reference.child("images").child(imageName) //images altında uuid ile kayıt.

        imageReference.putFile(imageUri).addOnSuccessListener {
            val uploadPictureReference = firebaseStorage.reference.child("images").child(imageName)
            uploadPictureReference.downloadUrl.addOnSuccessListener {
                val downloadUrl = it.toString() // resmin storage deki yolunu, firestore a kaydedecegiz. Yer kaplamaması icin path i kaydediyoruz.
                val postMap = hashMapOf<String,Any>()
                postMap.put("downloadImageUrl",downloadUrl)
                postMap.put("comment",comments)
                postMap.put("title",title)
                postMap.put("userEmail",auth.currentUser!!.email.toString()) // kullanıcı bu ekrana geldigince null olamayacagi icin !! kullanabiliriz.
                postMap.put("date",Timestamp.now())

                firestore.collection("Posts").add(postMap).addOnSuccessListener {
                    findNavController().navigate(R.id.homepageFragment,null, navOptions())
                }
                    .addOnFailureListener { errorFirestore ->
                    Toast.makeText(requireContext(),errorFirestore.localizedMessage,Toast.LENGTH_LONG).show()
                        println("fail fireStore save: ${errorFirestore.localizedMessage}")
                }
            }
        }
            .addOnFailureListener { errorFirebaseStorage ->
                Toast.makeText(requireContext(),errorFirebaseStorage.localizedMessage,Toast.LENGTH_LONG).show()
                println(errorFirebaseStorage.localizedMessage)
            }
    }


    private fun navOptions() :NavOptions {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.uploadFragment, true) // UploadFragment'ı stack'ten kaldır, geri dönülemesin save den sonra.
            .setLaunchSingleTop(true) // Tek bir instance çalıştır
            .build()

        return navOptions
    }

}