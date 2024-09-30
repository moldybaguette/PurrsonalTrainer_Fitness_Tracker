package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.FragmentPasswordReentryBinding

/**
 * A fragment that handles password reentry.
 */
class PasswordReentryFragment : Fragment() {

    private var listener: OnPasswordReentryListener? = null
    private var _binding: FragmentPasswordReentryBinding? = null
    private val binding get() = _binding!!

    /**
     * Interface to handle password reentry actions.
     */
    interface OnPasswordReentryListener {
        /**
         * Called when the password is reentered.
         * @param password The reentered password.
         */
        fun onPasswordReentered(password: String)
    }

    /**
     * Called when the fragment is attached to its context.
     * @param context The context to which the fragment is attached.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnPasswordReentryListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnPasswordReentryListener")
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordReentryBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called immediately after onCreateView has returned, but before any saved state has been restored in to the view.
     * @param view The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.doneButtonPasswordReentry.setOnClickListener {
            val password = binding.passwordInput.text.toString()
            listener?.onPasswordReentered(password)
        }
    }

    /**
     * Called when the fragment is no longer attached to its activity.
     */
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * Called when the view previously created by onCreateView has been detached from the fragment.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}