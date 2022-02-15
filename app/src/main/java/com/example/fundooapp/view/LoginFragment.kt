package com.example.fundooapp.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.R
import com.example.fundooapp.model.User
import com.example.fundooapp.service.UserAuthService
import com.example.fundooapp.viewmodel.LoginViewModel
import com.example.fundooapp.viewmodel.LoginViewModelFactory
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var forgotPassword: TextView
    private lateinit var signUp: TextView
    private lateinit var logInButton: Button
    private lateinit var googleLoginFab: FloatingActionButton
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var gso: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login_, container, false)
        emailText = view.findViewById(R.id.logInEmail)
        passwordText = view.findViewById(R.id.logIn_Password)
        forgotPassword = view.findViewById(R.id.forgot_password)
        signUp = view.findViewById(R.id.signUp)
        logInButton = view.findViewById(R.id.logIn_Button)
        firebaseAuth = FirebaseAuth.getInstance()
        googleLoginFab = view.findViewById(R.id.googleIcon)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory(UserAuthService())) [SharedViewModel::class.java]
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(UserAuthService())) [LoginViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = activity?.let { GoogleSignIn.getClient(it, gso) }!!

        logInButton.setOnClickListener { login() }
        googleLoginFab.setOnClickListener { signIn() }
        signUp.setOnClickListener { goToSignUpPage() }

    }

    companion object {
        private const val RC_SIGN_IN = 50
    }

    private fun login() {
        val email = emailText.text.toString()
        val password = passwordText.text.toString()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.error = "Invalid email format"
        } else if (TextUtils.isEmpty(password)) {
            passwordText.error = "Please enter password"
        } else {
            fireBaseLogin()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                loginViewModel.googleSignIn(account.idToken!!)
                loginViewModel.googleLoginStatus.observe(viewLifecycleOwner, Observer {
                    if (it.status) {
                        sharedViewModel.setGoToHomePageStatus(true)
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: ApiException) {
                Toast.makeText(context, "Google sign in failed $e", Toast.LENGTH_SHORT).show()
                Log.d("LoginFragment", "onActivityResult: ${e.printStackTrace()}")
            }
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun fireBaseLogin() {
        val email = emailText.text.toString().trim()
        val password = passwordText.text.toString().trim()
        user = User(email = email, password = password)
        loginViewModel.fundooLogIn(user)
        loginViewModel.loginStatus.observe(viewLifecycleOwner, Observer {
            if (it.status) {
                sharedViewModel.setGoToHomePageStatus(true)
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
            else {
                sharedViewModel.setGoToLoginPageStatus(true)
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun goToSignUpPage() {
        sharedViewModel.setGoToRegistrationPageStatus(true)
    }
}