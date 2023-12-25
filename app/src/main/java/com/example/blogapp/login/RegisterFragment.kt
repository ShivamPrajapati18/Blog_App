package com.example.blogapp.login

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.blogapp.R
import com.example.blogapp.databinding.FragmentRegisterBinding
import com.example.blogapp.model.User
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var mAuth: FirebaseAuth
    private var profileImage: Uri? = null
    private val userViewModel: UserViewModel by viewModels<UserViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        _binding = FragmentRegisterBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.registerBtn.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            Toast.makeText(context, "Register button clicked", Toast.LENGTH_SHORT).show()
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = User(mAuth.uid!!, name, email)
                    userViewModel.addUSerinDb(user = user)
                    //checking profileImage is not Null and add into the firebase storage
                    profileImage?.let { profileImage->
                        userViewModel.addProfileImage(mAuth.uid!!,profileImage)
                    }
                    findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                } else {
                    Toast.makeText(
                        context, task.exception?.localizedMessage, Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        binding.profileImage.setOnClickListener {
            getImage.launch("image/*")
        }
    }

    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) {uri->
        uri?.let {
            binding.profileImage.setImageURI(it)
            profileImage=it
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}