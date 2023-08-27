package com.discopotatodevelopment.giros_app

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.discopotatodevelopment.giros_app.databinding.FragmentResetPassBinding
import java.sql.DriverManager
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class FragmentResetPass: Fragment() {

    private var _binding: FragmentResetPassBinding? = null
    private val binding get() = _binding!!

    private var check_pass = false

    override fun onCreate(savedInstanceState: Bundle?) {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reset_pass, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentResetPassBinding.bind(view)
        _binding = binding

        binding.apply {
            buttonReset.isEnabled = false
            buttonReset.alpha = 0.5F

            editTextPass1.addTextChangedListener {
                buttonReset_enable()
            }

            editTextPass2.addTextChangedListener {
                buttonReset_enable()
            }

            buttonReset.setOnClickListener {
                checkPass(editTextPass1.text.toString(), editTextPass2.text.toString())

                val executor: Executor = Executors.newSingleThreadExecutor()
                val handler = Handler(Looper.getMainLooper())

                executor.execute {
                    try {
                        //val jdbcUrl = "jdbc:postgresql://192.168.169.247:5432/ebalovojest"
                        val connection =
                            DriverManager.getConnection(jdbcUrl, "giros_app", "291039")

                        if (check_pass) {
                            //а тут надо прописать запросик на изменение бд

                            findNavController().navigate(R.id.action_fragmentResetPass_to_fragmentFirstEntrance)
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

            if (editTextPass1.text.isNotEmpty() && editTextPass2.text.isNotEmpty() &&
                editTextPass1.text.toString() == editTextPass2.text.toString()) {
                buttonReset.isEnabled = true
                buttonReset.alpha = 1F
            } else {
                buttonReset.isEnabled = false
                buttonReset.alpha = 0.5F
            }
        }
    }
}