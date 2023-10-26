package com.example.myapplication.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.databinding.ActivitySplashBinding
import com.example.myapplication.databinding.EmailDilogeBinding
import com.example.myapplication.databinding.OtpDilogeBinding
import com.example.myapplication.network.ApiState
import com.example.myapplication.utils.BEARER
import com.example.myapplication.utils.appVersion
import com.example.myapplication.utils.codeFromPhone
import com.example.myapplication.utils.deviceId
import com.example.myapplication.utils.emailPattern
import com.example.myapplication.utils.hideKeyboard
import com.example.myapplication.utils.os
import com.example.myapplication.utils.startClearActivity
import com.example.myapplication.utils.startNewActivity
import com.example.myapplication.utils.toast
import com.example.myapplication.utils.validate
import com.example.myapplication.viewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModels by viewModels<MainViewModel>()
    // private var bottomSheetDialog: BottomSheetDialog? = null
    private lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        bottomSheetDialog = BottomSheetDialog(this)

        // Login
        binding.btnLogin.setOnClickListener {
         //   if (validLogin()) {

                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        viewModels.getLogin(
                            binding.etEmail.text.toString(),
                            binding.etPassword.text.toString(),
                            deviceId(applicationContext),
                            appVersion,
                            os
                        ).collect {
                            when (it) {
                                is ApiState.Success -> {
                                    if (it.data?.result.equals("success")) {

                                        lifecycleScope.launch {
                                            viewModels.saveUserData(it.data?.data?.uid.toString(),it.data?.data?.name.toString(),it.data?.data?.email.toString(),it.data?.data?.phone_no.toString())
                                            startClearActivity(HomeActivity::class.java)
                                            toast(it.data?.msg.toString())
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

            //}
            hideKeyboard()
        }

        // ResetPassword
        binding.txtReset.setOnClickListener {
            emailDialog()
        }

        binding.btnSignUp.setOnClickListener {
            startNewActivity(RegistrationActivity::class.java)
        }
    }

    private fun validLogin(): Boolean {

        if (binding.etEmail.text.toString().isEmpty()) {
            binding.inputEmail.error = "Please enter valid email address"
            return false
        } else if (!binding.etEmail.text.toString().trim().matches(emailPattern)) {
            binding.inputEmail.error = "Invalid email address"
            return false
        } else {
            binding.inputEmail.error = null
        }


        if (binding.etPassword.text.toString().isEmpty()) {
            binding.inputPassword.error = "Please enter valid password"
            return false
        } else if (binding.etPassword.text.toString().length < 8) {
            binding.inputPassword.visibility = View.VISIBLE
            binding.inputPassword.error = "Please enter minimum 8 char password"
            return false
        } else {
            binding.inputPassword.error = null

        }
        return true
    }


    @SuppressLint("SetTextI18n")
    private fun emailDialog() {
        bottomSheetDialog.setCancelable(false)
        val bottomBinding = EmailDilogeBinding.inflate(LayoutInflater.from(this))
        bottomSheetDialog.setContentView(bottomBinding.root)

        bottomBinding.btnOtp.setOnClickListener {
            if (bottomBinding.etEmail.text.toString().isEmpty()) {
                bottomBinding.inputEmail.error = "Email Can't be Empty"
            } else {
                bottomBinding.inputEmail.error = null
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        viewModels.resetPassword(bottomBinding.etEmail.text.toString()).collect { it ->
                            when (it) {

                                is ApiState.Loading -> {
                                    bottomBinding.progressBar.visibility = View.VISIBLE
                                }

                                is ApiState.Failure -> {

                                    bottomBinding.progressBar.visibility = View.GONE

                                    /* Snackbar.make(
                                         binding.root,
                                         it.exception,
                                         Snackbar.LENGTH_SHORT
                                     ).show()*/

                                    toast(it.message)

                                }

                                is ApiState.Success -> {

                                    bottomBinding.progressBar.visibility = View.GONE

                                    if (it.data?.result.equals("success")) {

                                        lifecycleScope.launch {
                                            bottomSheetDialog.dismiss()
                                            // OTP verify Screen
                                            otpVerifyDialog(bottomBinding.etEmail.text.toString())

                                            toast(it.data?.msg.toString())
                                        }

                                    } else {
                                        /* Snackbar.make(
                                             binding.root,
                                             it.data.message,
                                             Snackbar.LENGTH_SHORT
                                         ).show()*/
                                        toast(it.data?.msg.toString())
                                    }

                                }
                            }
                        }
                    }
                }
            }

        }

        bottomBinding.txtClear.setOnClickListener { bottomSheetDialog.dismiss() }

        bottomSheetDialog.show()
    }


    @SuppressLint("SetTextI18n")
    private fun otpVerifyDialog(email: String) {
        bottomSheetDialog.setCancelable(false)
        val bottomBinding = OtpDilogeBinding.inflate(LayoutInflater.from(this))
        bottomSheetDialog.setContentView(bottomBinding.root)

        bottomBinding.txtEdit.text = HtmlCompat.fromHtml(
            //"Please verify your Email Code sent to \n $email <font color=#305593>Edit</font> ",
            "Please verify your Email Code sent to \n $email ",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )


        bottomBinding.btnVerify.setOnClickListener {

            if (bottomBinding.etOtp.text?.length == 6) {
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        viewModels.verifyOtp(
                            email,
                            bottomBinding.etOtp.text.toString(),
                            "forgot password",
                            "",
                            ""
                        )
                            .collect {
                                when (it) {

                                    is ApiState.Loading -> {
                                        bottomBinding.progressBar.visibility = View.VISIBLE
                                    }

                                    is ApiState.Failure -> {

                                        bottomBinding.progressBar.visibility = View.GONE

                                        Snackbar.make(
                                            binding.root,
                                            it.message,
                                            Snackbar.LENGTH_SHORT
                                        ).show()

                                        bottomBinding.inputOtp.error = it.message


                                    }

                                    is ApiState.Success -> {

                                        bottomBinding.progressBar.visibility = View.GONE

                                        if (it.data?.statusCode == 200) {

                                            bottomBinding.passwordLayout.visibility = View.VISIBLE
                                            bottomBinding.otpLayout.visibility = View.GONE
                                            bottomBinding.txtTitle.text = "Change Password"


                                        } else {
                                            Snackbar.make(
                                                binding.root,
                                                it.data?.message.toString(),
                                                Snackbar.LENGTH_SHORT
                                            ).show()
                                        }

                                    }
                                }
                            }
                    }
                }
            } else {
                bottomBinding.inputOtp.error = "Enter 6 Digit Code"
            }


        }

        bottomBinding.txtResendOTP.setOnClickListener {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModels.resendOtp(email, "forgot password", "").collect { it ->
                        when (it) {

                            is ApiState.Loading -> {
                                bottomBinding.progressBar.visibility = View.VISIBLE
                            }

                            is ApiState.Failure -> {

                                bottomBinding.progressBar.visibility = View.GONE

                                Snackbar.make(
                                    binding.root,
                                    it.message.toString(),
                                    Snackbar.LENGTH_SHORT
                                ).show()


                            }

                            is ApiState.Success -> {

                                bottomBinding.progressBar.visibility = View.GONE

                                if (it.data?.result.equals("success")) {

                                    lifecycleScope.launch {
                                        toast(it.data?.msg.toString())
                                    }

                                } else {
                                    Snackbar.make(
                                        binding.root,
                                        it.data?.msg.toString(),
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }

                            }
                        }
                    }
                }
            }

        }

        bottomBinding.btnConfirm.setOnClickListener {
            if (validPassword(bottomBinding.inputChangePassword, bottomBinding.etChangePassword)) {
                bottomBinding.inputChangePassword.error = null
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        viewModels.changePassword(
                            email,
                            bottomBinding.etChangePassword.text.toString(),
                            bottomBinding.etOtp.text.toString()
                        ).collect { it ->
                            when (it) {

                                is ApiState.Loading -> {
                                    bottomBinding.progressBar.visibility = View.VISIBLE
                                }

                                is ApiState.Failure -> {

                                    bottomBinding.progressBar.visibility = View.GONE


                                    Snackbar.make(
                                        binding.root,
                                        it.message,
                                        Snackbar.LENGTH_SHORT
                                    ).show()

                                    bottomBinding.inputOtp.error = it.message


                                }

                                is ApiState.Success -> {

                                    bottomBinding.progressBar.visibility = View.GONE

                                    if (it.data?.statusCode == 200) {

                                        bottomSheetDialog.dismiss()

                                        lifecycleScope.launch {
                                            startClearActivity(HomeActivity::class.java)
                                            // toast("SignIn Successfully")
                                        }

                                    } else {
                                        Snackbar.make(
                                            binding.root,
                                            it.data?.message.toString(),
                                            Snackbar.LENGTH_SHORT
                                        ).show()
                                    }

                                }
                            }
                        }
                    }
                }
            } else {
                bottomBinding.inputOtp.error = "Enter 6 Digit Code"
            }
        }

        bottomBinding.txtClear.setOnClickListener { bottomSheetDialog?.dismiss() }
        // txtEdit?.setOnClickListener { bottomSheetDialog?.dismiss() }


        bottomSheetDialog.show()
    }

    private fun validPassword(
        inputChangePassword: TextInputLayout?,
        etChangePassword: TextInputEditText?,
    ): Boolean {
        if (etChangePassword?.text.toString().isEmpty()) {
            inputChangePassword?.error = "Please enter a valid password"
            return false
        } else if (etChangePassword?.text.toString().length < 6) {
            inputChangePassword?.error = "Please enter minimum 6 char password"
            return false
        } else if (!validate(etChangePassword?.text.toString())) {
            inputChangePassword?.error = "Please enter a valid password"
            return false
        } else {
            inputChangePassword?.error = "Please enter a valid password"

        }
        return true
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
        const val TAG = "LoginActivity"
    }
}