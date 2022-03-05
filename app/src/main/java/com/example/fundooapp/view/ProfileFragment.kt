package com.example.fundooapp.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.fundooapp.R
import com.example.fundooapp.model.Constant
import com.example.fundooapp.model.SharedPreference
import com.example.fundooapp.service.UserAuthService
import com.example.fundooapp.viewmodel.ProfileViewModel
import com.example.fundooapp.viewmodel.ProfileViewModelFactory
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : DialogFragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var signOutButton: Button
    private lateinit var profilePicture: ShapeableImageView
    private lateinit var cancelButton: ImageButton
    private lateinit var userEmail: TextView
    private lateinit var userName: TextView
    private val CAMERA_REQUEST = 100
    private val STORAGE_REQUEST = 200
    private val IMAGEPICK_GALLERY_REQUEST = 300
    private val IMAGE_PICKCAMERA_REQUEST = 400
    private lateinit var imageUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        profilePicture = view.findViewById(R.id.profile_picture)
        signOutButton = view.findViewById(R.id.signOut)
        userEmail = view.findViewById(R.id.email_viewer)
        userName = view.findViewById(R.id.name_viewer)
        cancelButton = view.findViewById(R.id.close_profile_fragment)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory(UserAuthService())) [SharedViewModel::class.java]
        profileViewModel = ViewModelProvider(this, ProfileViewModelFactory(UserAuthService())) [ProfileViewModel::class.java]
        firebaseAuth = FirebaseAuth.getInstance()
        profileViewModel.getData(requireContext())
        SharedPreference.initSharedPreferences(requireContext())
        displayUserData()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cancelButton.setOnClickListener { dismiss() }
        profilePicture.setOnClickListener { showImagePicDialog() }
        signOutButton.setOnClickListener { logOut() }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showImagePicDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Pick Image From")
        builder.setItems(options,  DialogInterface.OnClickListener { dialog, which ->
            if (which == 0) {
                if (!checkCameraPermission()!!) {
                    requestCameraPermission()
                } else {
                    pickFromCamera()
                }
            } else if (which == 1) {
                if (!checkStoragePermission()!!) {
                    requestStoragePermission()
                } else {
                    pickFromGallery()
                }
            }
        })
        builder.create().show()
    }

    private fun pickFromCamera() {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_pic")
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description")
        imageUri = requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, IMAGE_PICKCAMERA_REQUEST)
    }

    private fun checkStoragePermission(): Boolean? {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_REQUEST)
    }

    private fun checkCameraPermission(): Boolean? {
        val result = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        val result1 = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        return result && result1
    }

    private fun requestCameraPermission() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), CAMERA_REQUEST)
    }

    private fun displayUserData() {
        userName.text = SharedPreference.getString(Constant.USER_NAME)
        userEmail.text = SharedPreference.getString(Constant.USER_EMAIL)
        var uri = SharedPreference.getString(Constant.USER_PROFILE_PICTURE).toUri()
        Glide.with(this).load(uri).into(profilePicture)
    }

    private fun pickFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, IMAGEPICK_GALLERY_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGEPICK_GALLERY_REQUEST || requestCode == IMAGE_PICKCAMERA_REQUEST) {
                if (data != null) {
                    imageUri = data.data!!
                    profileViewModel.uploadImage(imageUri, requireContext())
                    Glide.with(this).load(imageUri).into(profilePicture)
                }
            }
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun logOut(){
        firebaseAuth.signOut()
        sharedViewModel.setGoToLoginPageStatus(true)
    }
}