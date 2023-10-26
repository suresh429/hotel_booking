package com.example.myapplication.ui

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapplication.databinding.ActivityRegistrationBinding
import com.example.myapplication.model.RegistrationResponse
import com.example.myapplication.network.ApiState
import com.example.myapplication.utils.appVersion
import com.example.myapplication.utils.deviceId
import com.example.myapplication.utils.emailPattern
import com.example.myapplication.utils.hideKeyboard
import com.example.myapplication.utils.os
import com.example.myapplication.utils.startNewActivity
import com.example.myapplication.utils.toast
import com.example.myapplication.utils.validate
import com.example.myapplication.viewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private val viewModels by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        bottomSheetDialog = BottomSheetDialog(this)

        // signUp
        binding.btnSignUp.setOnClickListener {

            if (validRegister()) {
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        viewModels.getSignUp(
                            binding.etName.text.toString(),
                            binding.etPhone.text.toString(),
                            binding.etEmail.text.toString(),
                            binding.etPassword.text.toString(),
                            deviceId(applicationContext),
                            appVersion,
                            os
                        ).collect {

                            when (it) {
                                is ApiState.Success -> {
                                    if (it.data?.result.equals("success") && it.data?.data is RegistrationResponse.Data) {

                                        lifecycleScope.launch {
                                            // startClearActivity(HomeActivity::class.java)
                                            toast(it.data.msg)
                                            //verifyEmail(it.data.data.email)
                                        }

                                    } else {
                                        Snackbar.make(
                                            binding.root,
                                            it.data?.msg.toString(),
                                            Snackbar.LENGTH_SHORT
                                        ).show()
                                    }

                                    dismissLoading()
                                }

                                is ApiState.Failure -> {
                                    Snackbar.make(
                                        binding.root,
                                        it.message,
                                        Snackbar.LENGTH_SHORT
                                    ).show()

                                    dismissLoading()
                                }

                                ApiState.Loading -> {
                                    showLoading()
                                }
                            }

                        }
                    }
                }
            }
            hideKeyboard()
        }

        // nav Login
        binding.txtLogin.setOnClickListener {
            startNewActivity(LoginActivity::class.java)
        }

    }

    private fun validRegister(): Boolean {

        if (binding.etName.text.toString().isEmpty()) {
            binding.inputName.error = "First Name can't be empty"
            return false
        } else {
            binding.inputName.isErrorEnabled = false
        }

        if (binding.etEmail.text.toString().isEmpty()) {
            binding.inputEmail.error = "Email is required"

            return false
        } else if (!binding.etEmail.text.toString().trim().matches(emailPattern)) {
            binding.inputEmail.error = "Invalid email address"

            return false
        } else {
            binding.inputEmail.isErrorEnabled = false

        }


        /*  if (binding.etPhone.text.toString().isEmpty()) {
              // binding.inputPhone.error = "Invalid phone number"
              binding.txtMobileError.text = "please enter a valid phone number"
              binding.txtMobileError.isVisible = true
              return false
          } else if (!binding.etPhone.text.toString().trim().matches(phonePattern)) {
              //binding.inputPhone.error = "Invalid phone number"
              binding.txtMobileError.text = "please enter a valid phone number"
              binding.txtMobileError.isVisible = true
              return false
          } else {
              // binding.inputPhone.isErrorEnabled = false
              binding.txtMobileError.isVisible = false

          }*/


        if (binding.etPassword.text.toString().isEmpty()) {
            binding.inputPassword.error = "Please enter a valid password"
            return false
        } else if (binding.etPassword.text.toString().length < 6) {
            binding.inputPassword.visibility = View.VISIBLE
            binding.inputPassword.error = "Please enter minimum 6 char password"
            return false
        } else if (!validate(binding.etPassword.text.toString())) {
            binding.inputPassword.error = "Please enter a valid password"
            return false
        } else {
            binding.inputPassword.isErrorEnabled = false

        }
        return true
    }


    override fun onDestroy() {
        super.onDestroy()
        bottomSheetDialog.dismiss()
    }

    private fun showLoading() {
        binding.progressBar.progressLayout.visibility = View.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun dismissLoading() {
        binding.progressBar.progressLayout.visibility = View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    companion object {
        private const val TAG = "RegistrationActivity"
    }

}