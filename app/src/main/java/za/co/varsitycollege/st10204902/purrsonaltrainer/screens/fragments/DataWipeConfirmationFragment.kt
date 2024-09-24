package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.FragmentDataWipeConfirmationBinding

class DataWipeConfirmationFragment : Fragment() {
    private var listener: OnDataWipeConfirmationListener? = null
    private var _binding: FragmentDataWipeConfirmationBinding? = null
    private val binding get() = _binding!!

    interface OnDataWipeConfirmationListener{
        fun onDataWipeConfirmed()
        fun onDataWipeCancelled()
    }

    override fun onAttach(context: Context){
        super.onAttach(context)
        if(context is OnDataWipeConfirmationListener){
            listener = context
        } else {
            throw RuntimeException("$context must implement OnDataWipeConfirmationListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDataWipeConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.confirmButton.setOnClickListener {
            listener?.onDataWipeConfirmed()
        }

        binding.cancelButton.setOnClickListener {
            listener?.onDataWipeCancelled()
        }
    }

    override fun onDetach(){
        super.onDetach()
        listener = null
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }
}