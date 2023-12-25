package com.example.blogapp

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.blogapp.databinding.FragmentReadPostBinding
import com.example.blogapp.model.Post

class ReadPostFragment : Fragment() {

    private var _binding:FragmentReadPostBinding?=null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_read_post, container, false)
        _binding= FragmentReadPostBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val post= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("post", Post::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("post")
        }
        binding.postTitle.text=post?.title
        binding.description.text=post?.description
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}