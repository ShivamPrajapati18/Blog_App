package com.example.blogapp

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.blogapp.adapter.PostAdapter
import com.example.blogapp.addPost.PostViewModel
import com.example.blogapp.databinding.FragmentHomeBinding
import com.example.blogapp.model.Post
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PostViewModel>()

    @Inject
    lateinit var mAuth: FirebaseAuth
    private var profileImage: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        _binding = FragmentHomeBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.addPost.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addPostFragment)
        }
        viewModel.getPost(mAuth.uid!!)
        lifecycleScope.launch {
            viewModel.allPost.collect {
                binding.recyclerView.adapter = PostAdapter(it, ::onClickReadButton,mAuth.uid)
                binding.recyclerView.layoutManager = LinearLayoutManager(context)
            }
        }
        viewModel.getProfileImage(mAuth.uid!!)
        viewModel.profileImg.observe(viewLifecycleOwner) {
            profileImage = it
            Glide.with(requireParentFragment())
                .load(it)
                .centerCrop()
                .placeholder(R.drawable.baseline_person_24)
                .into(binding.profileImage)
        }
        binding.profileImage.setOnClickListener {
            val bundle = bundleOf("img" to profileImage.toString())
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment,bundle)
        }
    }

    private fun onClickReadButton(currentItem: Post) {
        val bundle = bundleOf("post" to currentItem)
        findNavController().navigate(R.id.action_homeFragment_to_readPostFragment, bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
