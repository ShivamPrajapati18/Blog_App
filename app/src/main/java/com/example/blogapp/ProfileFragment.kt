package com.example.blogapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.blogapp.adapter.PostAdapter
import com.example.blogapp.addPost.PostViewModel
import com.example.blogapp.databinding.FragmentProfileBinding
import com.example.blogapp.model.Post
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PostViewModel>()

    @Inject
    lateinit var mAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        _binding = FragmentProfileBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val img = arguments?.getString("img")
        Glide.with(requireParentFragment())
            .load(img)
            .centerCrop()
            .placeholder(R.drawable.baseline_person_24)
            .into(binding.profileImage)

        viewModel.getUserPost(mAuth.uid!!)
        lifecycleScope.launch {
            launch {
                viewModel.userPost.collect {listOfPost->
                    setUpAdapter(listOfPost)
                }

            }
            launch {
                viewModel.getUser(mAuth.uid!!)
                viewModel.user.collectLatest {
                    binding.name.text=it
                }
            }
        }
    }

    private fun onDelete(postID: String) {
        viewModel.deletePost(postID)
        viewModel.getUserPost(mAuth.uid!!)
        viewModel.deleteResult.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun onClickReadButton(currentItem: Post) {
        val bundle = bundleOf("post" to currentItem)
        findNavController().navigate(R.id.action_profileFragment_to_readPostFragment, bundle)
    }

    private fun onUpdateButton(currentItem: Post){
        val bundle= bundleOf("post" to currentItem)
        findNavController().navigate(R.id.action_profileFragment_to_addPostFragment,bundle)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onClickLikedButton(currentItem: Post){
        viewModel.updateLiked(currentItem,mAuth.uid!!)
        binding.userAllPost.adapter?.notifyDataSetChanged()
    }

    private fun setUpAdapter(item:List<Post>){
        if (item.isNotEmpty()){
            binding.userAllPost.adapter =
                PostAdapter(item, ::onClickReadButton, mAuth.uid, ::onDelete,::onUpdateButton,::onClickLikedButton)
            binding.userAllPost.layoutManager = LinearLayoutManager(context)
            //setting visibility
            binding.notPostLabel.visibility=View.GONE
        }else{
            binding.notPostLabel.visibility=View.VISIBLE
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}