package com.discopotatodevelopment.giros_app

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.discopotatodevelopment.giros_app.databinding.FragmentSignUpBinding
import java.sql.DriverManager
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class FragmentSignUp: Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private var check_input = false
    lateinit var id_user: String

    override fun onCreate(savedInstanceState: Bundle?) {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSignUpBinding.bind(view)
        _binding = binding

        binding.apply {
            buttonSignUp.isEnabled = false
            buttonSignUp.alpha = 0.5F

            buttonRegistredYetSignup.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentSignUp_to_fragmentFirstEntrance)
            }

            //ОПТИМИЗИРОВАТЬ!
            //нижу куча наблюдателей изменения полей
            //мб их можно чекать одновременно все
            textViewNameSignup.addTextChangedListener {
                buttonSignUp_enable()
            }

            textViewLoginLastnameSignup.addTextChangedListener {
                buttonSignUp_enable()
            }

            textViewEmailSignup.addTextChangedListener {
                buttonSignUp_enable()
            }

            textViewLoginPhoneSignup.addTextChangedListener {
                buttonSignUp_enable()
            }

            editTextPasswordSignup1.addTextChangedListener {
                buttonSignUp_enable()
            }

            editTextPasswordSignup2.addTextChangedListener {
                buttonSignUp_enable()
            }


            buttonSignUp.setOnClickListener {
                checkInput(textViewEmailSignup.text.toString(),
                    textViewLoginPhoneSignup.text.toString(),
                    editTextPasswordSignup1.text.toString(),
                    editTextPasswordSignup2.text.toString())

                val executor: Executor = Executors.newSingleThreadExecutor()
                val handler = Handler(Looper.getMainLooper())

                Thread.sleep(1000)
                //слабое место, придумать, как чекать окончание выполнения параллельного потока

                //надо создать в таблице users идентификатор terms, который будет подтверждать
                //соглашение пользователя с правами пользования
                executor.execute {
                    try {
                        //val jdbcUrl = "jdbc:postgresql://192.168.169.247:5432/ebalovojest"
                        val connection =
                            DriverManager.getConnection(jdbcUrl, "giros_app", "291039")
                        Log.d("check", "check bool ${check_input.toString()}")

                        if (check_input) {

                            val query =
                                connection.prepareStatement("select max(id_user) from users")
                            val result = query.executeQuery()

                            while (result.next()) {
                                id_user = (result.getInt("max") + 1).toString()
                            }
                            query.close()

                            val query_1 =
                                connection.prepareStatement(
                                    "INSERT INTO users (id_user, name_user_users, " +
                                            "surname_users, email, phone_number_users, password_user, access_user) " +
                                            "VALUES (${id_user}, '${textViewNameSignup.text.toString()}', '${textViewLoginLastnameSignup.text.toString()}', " +
                                            "'${textViewEmailSignup.text.toString()}', '${textViewLoginPhoneSignup.text.toString()}', " +
                                            "'${editTextPasswordSignup1.text.toString()}', true)"
                                ).executeUpdate()

                            findNavController().navigate(R.id.action_fragmentSignUp_to_main_screen)
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

    private fun checkInput(email: String, phone: String, pass1: String, pass2: String) {

        val require_num_symb = setOf('1','2','3','4','5','6','7','8','9','0')
        val require_upper_symb = setOf('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O',
            'P','Q','R','S','T','U','V','W','X','Y','Z')
        val require_lower_symb = setOf('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o',
            'p','q','r','s','t','u','v','w','x','y','z')
        val require_email = setOf("mail.", '@')

        val executor: Executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        executor.execute {
            try {

                //val jdbcUrl = "jdbc:postgresql://192.168.169.247:5432/ebalovojest"
                val connection = DriverManager.getConnection(jdbcUrl, "giros_app", "291039")

                val query = connection.prepareStatement(
                    "select id_user, email, password_user, access_user from users where " +
                            "email = '${email}' or phone_number_users = '${phone}'")

                this.check_input = pass1 == pass2 && pass1.length >= 6 &&
                        pass1.any(require_num_symb::contains) &&
                        pass1.any(require_upper_symb::contains) &&
                        pass1.any(require_lower_symb::contains) &&
                        email.any(require_email::contains) &&
                        query.executeQuery().next() == false

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

    private fun buttonSignUp_enable() {
        binding.apply {
            if (textViewNameSignup.text.isNotEmpty() &&
                textViewLoginLastnameSignup.text.isNotEmpty() &&
                textViewEmailSignup.text.isNotEmpty() &&
                textViewLoginPhoneSignup.text.isNotEmpty() &&
                editTextPasswordSignup1.text.isNotEmpty() &&
                editTextPasswordSignup1.text.toString() == editTextPasswordSignup2.text.toString() &&
                checkBoxTerms.isChecked) {
                buttonSignUp.isEnabled = true
                buttonSignUp.alpha = 1F
            } else {
                buttonSignUp.isEnabled = false
                buttonSignUp.alpha = 0.5F
            }
        }
    }
}