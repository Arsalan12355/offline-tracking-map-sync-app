package com.example.compass.smartprintertest.basefragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.example.compass.smartprintertest.viewModel.MapViewModel
import com.example.compass.smartprintertest.utils.PrefHelper


abstract class BaseViewFragment<B : ViewBinding>( private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> B) : Fragment() {
    private var _binding: B? = null
    val binding get() = _binding
    protected lateinit var activity: Activity
    lateinit var mContext: Context

    val mapViewModel: MapViewModel by activityViewModels()


    lateinit var prefHelper: PrefHelper


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as Activity
        mContext = context
    }


    abstract fun onFragmentReady(view: View, savedInstanceState: Bundle?)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = bindingInflater(inflater, container, false)
        return binding?.root
    }

    abstract fun onBackPress()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefHelper = PrefHelper.getPrefInstance(activity)


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBackPress()
        }
        onFragmentReady(view, savedInstanceState)

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


}
