package com.example.blogapp.addPost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.blogapp.R
import com.example.blogapp.databinding.FragmentAddPostBinding
import com.example.blogapp.model.Post
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class AddPostFragment : Fragment() {
    private var _binding: FragmentAddPostBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var mAuth: FirebaseAuth
    val viewModel by viewModels<PostViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_post, container, false)
        _binding = FragmentAddPostBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.postBtn.setOnClickListener {
            val postTitle = binding.title.text.toString()
            val postDescription = binding.description.text.toString()
            val authorId=mAuth.uid!!
            val date=SimpleDateFormat("dd-MM-yyyy").format(Date().time)
            if (postTitle.isEmpty() && postDescription.isEmpty()){
                Toast.makeText(context, "Fill The All Filed", Toast.LENGTH_SHORT).show()
            }else {
                //Creating post Object
                val post = Post(authorId, postTitle, postDescription, date)
                viewModel.addPost(post)
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}