package com.lalwani.sample.receiverapp

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.lalwani.sample.receiverapp.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("Range")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            val uri = Uri.parse("content://com.lalwani.sample.crossappprovider/data")
            val cursor = activity?.contentResolver?.query(uri, null, null, null, null)
            cursor?.let {
                while (it.moveToNext()) {
                    val packageName = it.getString(it.getColumnIndex("package_name"))
                    val installTime = it.getLong(it.getColumnIndex("install_time"))
                    Log.d("yyyy", "Package: $packageName, InstallTime: $installTime")
                    binding.textviewFirst.setText("Package: $packageName, InstallTime: $installTime")
                }
                it.close()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}