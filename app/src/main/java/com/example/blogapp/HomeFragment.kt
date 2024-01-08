package com.example.blogapp

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
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
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PostViewModel>()

    @Inject
    lateinit var mAuth: FirebaseAuth
    private var profileImage: Uri? = null
    private lateinit var postArrayList: ArrayList<Post>
    private lateinit var mAdapter: PostAdapter
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
        postArrayList=ArrayList()
        mAdapter=PostAdapter(::onClickReadButton,mAuth.uid,::onClickLikedButton)
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        binding.addPost.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addPostFragment)
        }
        viewModel.getPost(mAuth.uid!!)
        lifecycleScope.launch {
            viewModel.allPost.collect {listOfPost->
                mAdapter.updateItem(listOfPost)
                postArrayList.also {
                    it.addAll(listOfPost)
                }
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
        binding.searchView.setOnQueryTextListener(object :OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    filterData(it)
                }
                return true
            }
        })
    }

    private fun filterData(text:String){
        val newArrayList= ArrayList(postArrayList.filter {
            it.title.contains(text,true)
        })
        mAdapter.updateItem(newArrayList)
    }
    private fun onClickReadButton(currentItem: Post) {
        val bundle = bundleOf("post" to currentItem)
        findNavController().navigate(R.id.action_homeFragment_to_readPostFragment, bundle)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onClickLikedButton(currentItem: Post){
        viewModel.updateLiked(currentItem,mAuth.uid!!)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
