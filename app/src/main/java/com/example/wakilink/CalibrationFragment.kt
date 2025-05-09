package com.example.wakilink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wakilink.databinding.FragmentCalibrationBinding

class CalibrationFragment : Fragment() {
    private var _binding: FragmentCalibrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalibrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.startCalibrationButton.setOnClickListener {
            // Aquí irá la lógica para iniciar la calibración
            binding.calibrationStatus.text = "Calibración en progreso..."
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 