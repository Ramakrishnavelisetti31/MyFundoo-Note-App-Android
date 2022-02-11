package com.example.fundooapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.R
import com.example.fundooapp.service.UserAuthService
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_FundooApp)
        setContentView(R.layout.activity_main)

        sharedViewModel = ViewModelProvider(this, SharedViewModelFactory(UserAuthService())) [SharedViewModel::class.java]
        sharedViewModel.setGoToLoginPageStatus(true)
        observeAppNav()
    }

    private fun observeAppNav() {
        sharedViewModel.goToLoginPageStatus.observe(this, Observer {
            if (it == true) {
                goToLoginPage()
            }
        })
        sharedViewModel.goToRegistrationPageStatus.observe(this, Observer {
            if (it == true) {
                goToRegistrationPage()
            }
        })
        sharedViewModel.goToHomePageStatus.observe(this, Observer {
            if (it == true) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun goToLoginPage() {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerMain, LoginFragment()).addToBackStack(null).commit()
    }

    private fun goToRegistrationPage() {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerMain, RegisterFragment()).addToBackStack(null).commit()
    }
}