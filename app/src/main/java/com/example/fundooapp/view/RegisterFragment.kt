package com.example.fundooapp.view

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.R
import com.example.fundooapp.model.User
import com.example.fundooapp.service.UserAuthService
import com.example.fundooapp.viewmodel.RegisterViewModel
import com.example.fundooapp.viewmodel.RegisterViewModelFactory
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory

class RegisterFragment : Fragment() {
    private lateinit var fullNameText: EditText
    private lateinit var userNameText: EditText
    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var confirmPasswordText: EditText
    private lateinit var logIn: TextView
    private lateinit var signUpButton: Button
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         val view: View = inflater.inflate(R.layout.fragment_register_, container, false)
         fullNameText = view.findViewById(R.id.full_name)
         userNameText = view.findViewById(R.id.user_name)
         emailText =  view.findViewById(R.id.signUp_Email)
         passwordText = view.findViewById(R.id.signUp_password)
         confirmPasswordText = view.findViewById(R.id.confirm_Password)
         logIn = view.findViewById(R.id.logIn)
         signUpButton = view.findViewById(R.id.signUp_button)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory(UserAuthService())) [SharedViewModel::class.java]
        registerViewModel = ViewModelProvider(this, RegisterViewModelFactory(UserAuthService())) [RegisterViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpButton.setOnClickListener { signUp() }
        logIn.setOnClickListener {  goToLoginPage() }
    }

    private fun signUp() {
            val email = emailText.text.toString()
            val password = passwordText.text.toString()
            val confirmPassword = confirmPasswordText.text.toString()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailText.error = "Invalid email format"
            }
            else if (TextUtils.isEmpty(password)) {
                passwordText.error = "Please enter password"
            }
            else if (password.length < 6 ) {
                passwordText.error = "Password must at least 6 characters long"
            }
            else if (confirmPassword != password) {
                confirmPasswordText.error = "Password doesn't match enter again"
            }
            else {
                firebaseSignUp()
            }
        }

    private fun firebaseSignUp() {
        val fullName = fullNameText.text.toString()
        val userName = userNameText.text.toString()
        val email = emailText.text.toString()
        val password = passwordText.text.toString()
        val user = User(fullName = fullName, userId = userName, email = email, password = password)
        registerViewModel.fundooRegister(user)
        registerViewModel.registrationStatus.observe(viewLifecycleOwner, Observer {
            if (it.status) {
                sharedViewModel.setGoToHomePageStatus(true)
            }
            else {
                sharedViewModel.setGoToRegistrationPageStatus(true)
            }
        })
    }

    private fun goToLoginPage() {
        sharedViewModel.setGoToLoginPageStatus(true)
    }
}