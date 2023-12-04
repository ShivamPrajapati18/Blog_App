package com.example.blogapp.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.blogapp.R
import com.example.blogapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    @Inject
    lateinit var mAuth: FirebaseAuth
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.LoginBtn.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                //sign In the user
                mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(context, "Login Successfully", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        } else {
                            Toast.makeText(
                                context,
                                it.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
            }else{
                //setting the error message
                if (email.isEmpty()){
                    binding.email.error = "Enter the Email"
                }else{
                    binding.password.error="Enter the Password"
                }
            }
        }
        binding.signUpTxt.setOnClickListener {
            Toast.makeText(context, "Sign Up Clicked", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser!=null){
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}