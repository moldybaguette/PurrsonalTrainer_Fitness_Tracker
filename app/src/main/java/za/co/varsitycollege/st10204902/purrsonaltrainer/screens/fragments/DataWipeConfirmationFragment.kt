package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.FragmentDataWipeConfirmationBinding

/**
 * A fragment that displays a confirmation dialog for data wipe.
 */
class DataWipeConfirmationFragment : Fragment() {
    private var listener: OnDataWipeConfirmationListener? = null
    private var _binding: FragmentDataWipeConfirmationBinding? = null
    private val binding get() = _binding!!

    /**
     * Interface to handle data wipe confirmation actions.
     */
    interface OnDataWipeConfirmationListener {
        /**
         * Called when the data wipe is confirmed.
         */
        fun onDataWipeConfirmed()

        /**
         * Called when the data wipe is cancelled.
         */
        fun onDataWipeCancelled()
    }

    /**
     * Called when the fragment is attached to its context.
     * @param context The context to which the fragment is attached.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDataWipeConfirmationListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnDataWipeConfirmationListener")
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
        // Inflate the layout for this fragment
        _binding = FragmentDataWipeConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called immediately after onCreateView has returned, but before any saved state has been restored in to the view.
     * @param view The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.confirmButton.setOnClickListener {
            listener?.onDataWipeConfirmed()
        }

        binding.cancelButton.setOnClickListener {
            listener?.onDataWipeCancelled()
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