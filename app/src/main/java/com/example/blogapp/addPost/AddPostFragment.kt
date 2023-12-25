package com.example.blogapp.addPost

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.blogapp.R
import com.example.blogapp.databinding.FragmentAddPostBinding
import com.example.blogapp.model.Post
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class AddPostFragment : Fragment() {
    private var _binding: FragmentAddPostBinding? = null
    private val binding get() = _binding!!
    private var user: String=""
    @Inject
    lateinit var mAuth: FirebaseAuth
    private val viewModel: PostViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_post, container, false)
        _binding = FragmentAddPostBinding.bind(view)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val authorId = mAuth.uid!!
        viewModel.getUser(authorId)
        lifecycleScope.launch {
            viewModel.user.collectLatest {
                user=it
            }
        }
        binding.postBtn.setOnClickListener {
            val postTitle = binding.title.text.toString()
            val postDescription = binding.description.text.toString()
            val date = SimpleDateFormat("dd-MM-yyyy").format(Date().time)
            val postUid=UUID.randomUUID().toString()+System.currentTimeMillis().toString()
            if (postTitle.isEmpty() && postDescription.isEmpty()) {
                Toast.makeText(context, "Fill The All Filed", Toast.LENGTH_SHORT).show()
            } else {
                //Creating post Object
                val post = Post(postUid,authorId,user, postTitle, postDescription, date)
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