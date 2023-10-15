package com.discopotatodevelopment.giros_app.main.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.discopotatodevelopment.giros_app.*
import com.discopotatodevelopment.giros_app.databinding.FragmentForgotPassBinding
import java.sql.DriverManager
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class FragmentForgotPass(): Fragment() {

    private var _binding: FragmentForgotPassBinding? = null
    private val binding get() = _binding!!
    private var check_str = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_forgot_pass, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as ActivityMain).bottomNav.visibility = View.GONE

        (activity as? AppCompatActivity)?.supportActionBar?.title = ""

        view.findViewById<Toolbar>(R.id.toolbar).apply {
            setNavigationIcon(com.google.android.material.R.drawable.material_ic_keyboard_arrow_left_black_24dp)

            setNavigationOnClickListener {
                findNavController().navigate(R.id.action_fragmentForgotPass_to_fragmentFirstEntrance, null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.fragmentFirstEntrance, true)
                        .build())
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentForgotPassBinding.bind(view)
        _binding = binding

        binding.apply {

            //buttonReset.isEnabled = false
            buttonReset.alpha = 0.5F

            editTextLogin.addTextChangedListener {
                if (editTextLogin.text.isNotEmpty()) {
                    //buttonReset.isEnabled = true
                    buttonReset.alpha = 1F
                } else {//buttonReset.isEnabled = false
                    buttonReset.alpha = 0.5F}
            }

            buttonReset.setOnClickListener {
                if (buttonReset.text == "Reset") {
                    goToReset()
                } else {
                    retrieveCode()
                }
            }

            SignUpButton.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentForgotPass_to_fragmentSignUp)
            }

            LogInButton.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentForgotPass_to_fragmentFirstEntrance)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkInput(str: String) {
        val require_phone = setOf('1','2','3','4','5','6','7','8','9','0', '+')
        val require_email = setOf("mail.", '@')
        this.check_str = str.any(require_phone::contains) || str.any(require_email::contains)
    }

    private fun goToReset() {
        binding.apply {
            //checkInput(editTextLogin.text.toString())

            //тут проверка кода

            findNavController().navigate(R.id.action_fragmentForgotPass_to_fragmentResetPass)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun retrieveCode() {
        binding.apply {
            checkInput(editTextLogin.text.toString())

            val executor: Executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())

            executor.execute {
                try {
                    val connection = DriverManager.getConnection(jdbcUrl, user, password)

                    if (check_str) {
                        val query =
                            connection.prepareStatement("select email, phone_number_users from " +
                                    "users where email = '${editTextLogin.text.toString()}' or " +
                                    "phone_number_users = '${editTextLogin.text.toString()}'")

                        if (query.executeQuery().next()) {
                            editTextLogin.hint = "Code"
                            buttonReset.text = "Reset"

                            //тут отправлем код на почту или по смс
                        }
                    }

                    handler.post {
                        // update the result to the UI thread
                        // or any operation you want to perform on UI thread. It is similar to onPostExecute() of AsyncTask
                        editTextLogin.text.clear()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    handler.post { } // update error to UI thread or handle }
                }
            }
        }
    }
}