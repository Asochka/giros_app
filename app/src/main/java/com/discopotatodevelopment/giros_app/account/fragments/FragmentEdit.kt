package com.discopotatodevelopment.giros_app.account.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.discopotatodevelopment.giros_app.*
import com.discopotatodevelopment.giros_app.dataCoordinator.DataCoordinator
import com.discopotatodevelopment.giros_app.dataCoordinator.getuserID
import com.discopotatodevelopment.giros_app.databinding.FragmentEditBinding
import kotlinx.coroutines.launch
import java.sql.DriverManager

class FragmentEdit : Fragment() {

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch { loadAccPhoto() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_edit, container, false)

        val view = inflater.inflate(R.layout.fragment_edit, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as ActivityMain).bottomNav.visibility = View.GONE

        (activity as? AppCompatActivity)?.supportActionBar?.title = "Edit profile"

        view.findViewById<Toolbar>(R.id.toolbar).apply {
            setNavigationIcon(com.google.android.material.R.drawable.material_ic_keyboard_arrow_left_black_24dp)

            setNavigationOnClickListener {
                findNavController().navigate(R.id.action_fragmentEdit_to_account_screen, null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.account_screen, true)
                        .build())
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentEditBinding.bind(view)
        _binding = binding
    }


    override fun onDestroyView() {
        super.onDestroyView()
        (activity as ActivityMain).bottomNav.visibility = View.VISIBLE
    }

    private suspend fun loadAccPhoto() {
        val connection = DriverManager.getConnection(jdbcUrl, user, password)
        val id_user = DataCoordinator.shared.getuserID()

        val query = connection.prepareStatement(
            "select name_user_users, acc_photo from users where id_user = ${id_user}")
        val result = query.executeQuery()

        while (result.next()) {
            binding.loadBar.visibility = View.VISIBLE
            //binding.profileName.text = result.getString("name_user_users")
            binding.avatar.load(result.getString("acc_photo")) {
                transformations(CircleCropTransformation())
                listener { _, _ ->
                    binding.loadBar.visibility = View.GONE
                }
            }
        }
    }
}