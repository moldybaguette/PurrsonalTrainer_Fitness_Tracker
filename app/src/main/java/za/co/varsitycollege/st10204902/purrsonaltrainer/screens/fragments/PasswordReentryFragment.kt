package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.FragmentPasswordReentryBinding

class PasswordReentryFragment : Fragment() {

    private var listener: OnPasswordReentryListener? = null
    private var _binding: FragmentPasswordReentryBinding? = null
    private val binding get() = _binding!!

    interface OnPasswordReentryListener {
        fun onPasswordReentered(password: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnPasswordReentryListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnPasswordReentryListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordReentryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.doneButtonPasswordReentry.setOnClickListener {
            val password = binding.passwordInput.text.toString()
            listener?.onPasswordReentered(password)
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}