package com.example.blogapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blogapp.adapter.PostAdapter
import com.example.blogapp.addPost.PostViewModel
import com.example.blogapp.databinding.FragmentHomeBinding
import com.example.blogapp.model.Post
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding:FragmentHomeBinding?=null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PostViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_home, container, false)
        _binding=FragmentHomeBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.addPost.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addPostFragment)
        }
        val listPost= mutableListOf<Post>()
        viewModel.getPost()
        lifecycleScope.launch {
            viewModel.allPost.collect {
                binding.recyclerView.adapter=PostAdapter(it)
                binding.recyclerView.layoutManager=LinearLayoutManager(context)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}