package com.discopotatodevelopment.giros_app.account.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.discopotatodevelopment.giros_app.R
import com.discopotatodevelopment.giros_app.dataCoordinator.DataCoordinator
import com.discopotatodevelopment.giros_app.dataCoordinator.getuserID
import com.discopotatodevelopment.giros_app.databinding.FragmentAccountBinding
import com.discopotatodevelopment.giros_app.jdbcUrl
import com.discopotatodevelopment.giros_app.password
import com.discopotatodevelopment.giros_app.user
import kotlinx.coroutines.launch
import java.sql.DriverManager

class FragmentAccount : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch { loadAccPhoto() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAccountBinding.bind(view)
        _binding = binding

        binding.apply {
            editConstraint.setOnClickListener {
                findNavController().navigate(R.id.action_account_screen_to_fragmentEdit)
            }

            statConstraint.setOnClickListener {
                findNavController().navigate(R.id.action_account_screen_to_fragmentStatisitics)
            }

            payMethodConstraint.setOnClickListener {
                findNavController().navigate(R.id.action_account_screen_to_fragmentPayMethod)
            }

            notifConstraint.setOnClickListener {
                findNavController().navigate(R.id.action_account_screen_to_fragmentNotifications)
            }

            logOutConstraint.setOnClickListener {
                val modalbottomSheetFragment = FragmentLogOutBottomSheet()
                modalbottomSheetFragment.show(childFragmentManager, modalbottomSheetFragment.tag)
            }

            askQueConstraint.setOnClickListener {
                findNavController().navigate(R.id.action_account_screen_to_fragmentAskQuest)
            }

            termsConstraint.setOnClickListener {
                val uris = Uri.parse("https://owxcfcjmsnqnnwxlexsf.supabase.co/storage/v1/object/sign/User%20Terms/UserTerms.pdf?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1cmwiOiJVc2VyIFRlcm1zL1VzZXJUZXJtcy5wZGYiLCJpYXQiOjE2OTQ3OTU4MDAsImV4cCI6MTY5NzM4NzgwMH0.KTWjV72ZHmEKogUfU-bljoUErV-iokcimcjUJ89Eneg&t=2023-09-15T16%3A36%3A40.279Z")
                val intents = Intent(Intent.ACTION_VIEW, uris)
                val bundle = Bundle()
                bundle.putBoolean("new_window", true)
                intents.putExtras(bundle)
                requireContext().startActivity(intents)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun loadAccPhoto() {
        val connection = DriverManager.getConnection(jdbcUrl, user, password)
        val id_user = DataCoordinator.shared.getuserID()

        val query = connection.prepareStatement(
            "select name_user_users, acc_photo from users where id_user = ${id_user}")
        val result = query.executeQuery()

        while (result.next()) {
            binding.loadBar.visibility = View.VISIBLE
            binding.profileName.text = result.getString("name_user_users")
            binding.avatar.load(result.getString("acc_photo")) {
                transformations(CircleCropTransformation())
                listener { _, _ ->
                    binding.loadBar.visibility = View.GONE
                }
            }
        }
    }
}