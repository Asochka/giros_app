package com.discopotatodevelopment.giros_app.main.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.discopotatodevelopment.giros_app.*
import com.discopotatodevelopment.giros_app.dataCoordinator.DataCoordinator
import com.discopotatodevelopment.giros_app.dataCoordinator.getisLogin
import com.discopotatodevelopment.giros_app.dataCoordinator.updateisLogin
import com.discopotatodevelopment.giros_app.dataCoordinator.updateuserID
import com.discopotatodevelopment.giros_app.databinding.FragmentFirstEntranceBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.sql.DriverManager
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class FragmentFirstEntrance: Fragment(){

    private var _binding: FragmentFirstEntranceBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {checkLogin()}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_first_entrance, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentFirstEntranceBinding.bind(view)
        _binding = binding

        //binding.LogInButton.isEnabled = false
        binding.LogInButton.alpha = 0.5F

        binding.editTextLogin.addTextChangedListener {editTextLogin_enable()}

        binding.editTextPasswordLogin.addTextChangedListener {editTextLogin_enable()}

        binding.LogInButton.setOnClickListener {
            val executor: Executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())

            executor.execute {
                try {
                    // perform task asynchronously
                    // you can also execute runnable or callable
                    val connection = DriverManager.getConnection(jdbcUrl, user, password)

                    val query = connection.prepareStatement(
                        "select id_user, email, password_user, access_user from users where " +
                                "email = '${binding.editTextLogin.text}' and password_user = '${binding.editTextPasswordLogin.text}'")

                    val result = query.executeQuery()
                    while (result.next()) {
                        if (result.getString("email") == binding.editTextLogin.text.toString() &&
                            result.getString("password_user") == binding.editTextPasswordLogin.text.toString()) {

                            DataCoordinator.shared.updateuserID(result.getInt("id_user"))
                            if (binding.rememberCheckBox.isChecked) {
                                DataCoordinator.shared.updateisLogin(true)
                            } else {DataCoordinator.shared.updateisLogin(false)}
                            findNavController().navigate(R.id.action_fragmentFirstEntrance_to_main_screen)
                        } else if (result.getString("email") != binding.editTextLogin.text.toString()) {
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
        if (binding.editTextLogin.text!!.isNotEmpty() &&
            binding.editTextPasswordLogin.text!!.isNotEmpty()) {
            //binding.LogInButton.isEnabled = true
            binding.LogInButton.alpha = 1F
        } else {//binding.LogInButton.isEnabled = false
            binding.LogInButton.alpha = 0.5F}
    }

    suspend fun checkLogin() {
        if (DataCoordinator.shared.getisLogin()) {
            findNavController().navigate(R.id.action_fragmentFirstEntrance_to_main_screen)
            (activity as ActivityMain).bottomNav.visibility = View.VISIBLE
        } else {
            (activity as ActivityMain).bottomNav.visibility = View.GONE
        }
    }
}