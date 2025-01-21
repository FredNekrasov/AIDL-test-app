package com.fredprojects.presentation.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.fredprojects.domain.model.User
import com.fredprojects.presentation.core.getMessageId
import com.fredprojects.presentation.databinding.FragmentUserBinding
import com.fredprojects.presentation.vm.UserState
import com.fredprojects.presentation.vm.UserVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to create an instance of this fragment.
 */
@AndroidEntryPoint
class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding ?: error("exception in the UserFragment")
    private val userVM by viewModels<UserVM>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View { // Inflate the layout for this fragment
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }
    private fun setUpViews() = with(binding) {
        viewLifecycleOwner.lifecycleScope.launch {
            userVM.userStateSF.collect {
                onStateChange(it)
            }
        }
        getButton.setOnClickListener {
            userVM.getUser()
        }
        setButton.setOnClickListener {
            val login = loginET.text.toString()
            val user = User(login, (0..100).random())
            userVM.setUser(user)
        }
    }
    private fun onStateChange(state: UserState) = with(binding) {
        progressLayout.isVisible = state.isLoading
        userLabel.text = state.user?.toString() ?: "user not found"
        state.userDataStatus?.let { status ->
            statusLabel.text = getString(status.getMessageId())
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        @JvmStatic
        fun newInstance() = UserFragment()
    }
}