package com.example.blogapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.blogapp.databinding.FragmentLoginBinding
import com.example.blogapp.databinding.FragmentRegisterBinding
import com.example.blogapp.login.UserViewModel
import com.example.blogapp.model.User
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding:FragmentRegisterBinding?=null
    val binding get() = _binding!!
    @Inject
    lateinit var mAuth: FirebaseAuth
    private val userViewModel: UserViewModel by viewModels<UserViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_register, container, false)
        _binding= FragmentRegisterBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.registerBtn.setOnClickListener {
            val name=binding.name.text.toString()
            val email=binding.email.text.toString()
            val pass=binding.password.text.toString()
            Toast.makeText(context, "Register button clicked", Toast.LENGTH_SHORT).show()
            mAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener {task->
                    if (task.isSuccessful){
                        val user=User(mAuth.uid!!,name, email)
                        userViewModel.addUSerinDb(user = user)
                        findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                    }else{
                        Toast.makeText(context, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}