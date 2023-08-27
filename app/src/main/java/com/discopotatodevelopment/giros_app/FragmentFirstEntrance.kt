package com.discopotatodevelopment.giros_app

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.discopotatodevelopment.giros_app.databinding.FragmentFirstEntranceBinding
import java.sql.DriverManager
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class FragmentFirstEntrance: Fragment() {

    private var _binding: FragmentFirstEntranceBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first_entrance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentFirstEntranceBinding.bind(view)
        _binding = binding

        binding.LogInButton.isEnabled = false
        binding.LogInButton.alpha = 0.5F

        binding.editTextLogin.addTextChangedListener {
            editTextLogin_enable()
        }

        binding.editTextPasswordLogin.addTextChangedListener {
            editTextLogin_enable()
        }

        binding.LogInButton.setOnClickListener {

            val executor: Executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())

            executor.execute {
                try {
                    // perform task asynchronously
                    // you can also execute runnable or callable
                    //val jdbcUrl = "jdbc:postgresql://192.168.222.247:5432/ebalovojest"
                    val connection = DriverManager.getConnection(jdbcUrl, "giros_app", "291039")

                    val query = connection.prepareStatement(
                        "select id_user, email, password_user, access_user from users where " +
                                "email = '${binding.editTextLogin.text}' and password_user = '${binding.editTextPasswordLogin.text}'"
                    )

                    val result = query.executeQuery()
                    while (result.next()) {
                        if (result.getString("email") == binding.editTextLogin.text.toString() &&
                            result.getString("password_user") == binding.editTextPasswordLogin.text.toString()) {
                            Log.d("login","i'm log in")
                            findNavController().navigate(R.id.action_fragmentFirstEntrance_to_main_screen)
                        } else if (result.getString("email") != binding.editTextLogin.text.toString() &&
                            result.getString("password_user") == binding.editTextPasswordLogin.text.toString()) {
                            val toast = Toast.makeText(context, "We couldn't find that email. Check it out. ", Toast.LENGTH_SHORT)
                            toast.setGravity(Gravity.CENTER, 0, 0)
                            toast.show()
                        } else if (result.getString("password_user") != binding.editTextPasswordLogin.text.toString()) {
                            val toast = Toast.makeText(context, "Oops, wrong password. Try again", Toast.LENGTH_SHORT)
                            toast.setGravity(Gravity.CENTER, 0, 0)
                            toast.show()
                        }
                    }

                    handler.post {
                        // update the result to the UI thread
                        // or any operation you want to perform on UI thread. It is similar to onPostExecute() of AsyncTask
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    handler.post { // update error to UI thread or handle }
                    }
                }
            }
        }

        binding.SignUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentFirstEntrance_to_fragmentSignUp)
        }

        binding.ResetPass.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentFirstEntrance_to_fragmentForgotPass)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun editTextLogin_enable() {
        if (binding.editTextLogin.text.isNotEmpty() &&
            binding.editTextPasswordLogin.text.isNotEmpty()) {
            binding.LogInButton.isEnabled = true
            binding.LogInButton.alpha = 1F
        } else {binding.LogInButton.isEnabled = false
            binding.LogInButton.alpha = 0.5F}
    }
}