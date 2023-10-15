package com.discopotatodevelopment.giros_app.main.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.discopotatodevelopment.giros_app.ActivityMain
import com.discopotatodevelopment.giros_app.R
import com.discopotatodevelopment.giros_app.databinding.FragmentResetPassBinding
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class FragmentResetPass: Fragment() {

    private var _binding: FragmentResetPassBinding? = null
    private val binding get() = _binding!!

    private var check_pass = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return inflater.inflate(R.layout.fragment_reset_pass, container, false)

        val view = inflater.inflate(R.layout.fragment_reset_pass, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as ActivityMain).bottomNav.visibility = View.GONE

        (activity as? AppCompatActivity)?.supportActionBar?.title = ""

        view.findViewById<Toolbar>(R.id.toolbar).apply {
            setNavigationIcon(com.google.android.material.R.drawable.material_ic_keyboard_arrow_left_black_24dp)

            setNavigationOnClickListener {
                findNavController().navigate(R.id.action_fragmentResetPass_to_fragmentForgotPass, null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.fragmentForgotPass, true)
                        .build())
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentResetPassBinding.bind(view)
        _binding = binding

        binding.apply {
            //buttonReset.isEnabled = false
            buttonReset.alpha = 0.5F

            editTextPasswordReset1.addTextChangedListener {
                buttonReset_enable()
            }

            editTextPasswordReset2.addTextChangedListener {
                buttonReset_enable()
            }

            buttonReset.setOnClickListener {
                checkPass(editTextPasswordReset1.text.toString(), editTextPasswordReset2.text.toString())

                val executor: Executor = Executors.newSingleThreadExecutor()
                val handler = Handler(Looper.getMainLooper())

                executor.execute {
                    try {
                        //val connection = DriverManager.getConnection(jdbcUrl, user, password)

                        if (check_pass) {
                            //а тут надо прописать запросик на изменение бд

                            findNavController().navigate(R.id.action_fragmentResetPass_to_fragmentFirstEntrance)
                            (activity as ActivityMain).bottomNav.visibility = View.VISIBLE
                        }

                        handler.post {
                            // update the result to the UI thread
                            // or any operation you want to perform on UI thread. It is similar to onPostExecute() of AsyncTask
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        handler.post { } // update error to UI thread or handle }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkPass(pass1: String, pass2: String){
        val require_num_symb = setOf('1','2','3','4','5','6','7','8','9','0')
        val require_upper_symb = setOf('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O',
            'P','Q','R','S','T','U','V','W','X','Y','Z')
        val require_lower_symb = setOf('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o',
            'p','q','r','s','t','u','v','w','x','y','z')

        this.check_pass = pass1 == pass2 && pass1.length >= 6 &&
                pass1.any(require_num_symb::contains) &&
                pass1.any(require_upper_symb::contains) &&
                pass1.any(require_lower_symb::contains)
    }

    private fun buttonReset_enable() {
        binding.apply {

            if (editTextPasswordReset1.text.isNotEmpty() && editTextPasswordReset2.text.isNotEmpty() &&
                editTextPasswordReset1.text.toString() == editTextPasswordReset2.text.toString()) {
                //buttonReset.isEnabled = true
                buttonReset.alpha = 1F
            } else {
                //buttonReset.isEnabled = false
                buttonReset.alpha = 0.5F
            }
        }
    }
}