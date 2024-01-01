package com.example.blogapp.addPost

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
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
    private var user: String = ""

    @Inject
    lateinit var mAuth: FirebaseAuth
    private val viewModel: PostViewModel by viewModels()
    private var isUpdateForPost: Post? = null
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
                user = it
            }
        }
        isUpdateForPost = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("post", Post::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("post")
        }

        //handling visibility of add and update button
        if (isUpdateForPost != null) {
            binding.addPostBtn.visibility = View.GONE
            binding.updatePost.visibility = View.VISIBLE
            // set the text for edit
            binding.title.setText(isUpdateForPost?.title)
            binding.description.setText(isUpdateForPost?.description)
        } else {
            binding.addPostBtn.visibility = View.VISIBLE
            binding.updatePost.visibility = View.GONE
        }
        binding.addPostBtn.setOnClickListener {
            val postTitle = binding.title.text.toString()
            val postDescription = binding.description.text.toString()
            val date = SimpleDateFormat("dd-MM-yyyy").format(Date().time)
            val postUid = UUID.randomUUID().toString() + System.currentTimeMillis()
            if (postTitle.isEmpty() && postDescription.isEmpty()) {
                Toast.makeText(context, "Fill The All Filed", Toast.LENGTH_SHORT).show()
            } else {
                //Creating post Object
                val post = Post(postUid, authorId, user, postTitle, postDescription, date)
                viewModel.addPost(post)
                findNavController().popBackStack()
            }
        }

        binding.updatePost.setOnClickListener {
            val postTitle = binding.title.text.toString()
            val postDescription = binding.description.text.toString()
            val date = SimpleDateFormat("dd-MM-yyyy").format(Date().time)
            val post = isUpdateForPost!!
            if ((postTitle.isEmpty() && postDescription.isEmpty())){
                Toast.makeText(context, "Fill The All Filed", Toast.LENGTH_SHORT).show()
            }else{
                if (post.title != postTitle || post.description != postDescription){
                    val updatePost = Post(post.postId,post.authorId,post.authorName, postTitle,postDescription,date)
                    viewModel.updatePost(updatePost)
                    findNavController().popBackStack()
                }else{
                    Toast.makeText(context, "No changes", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}