package com.example.wolmoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ar.com.wolox.wolmo.core.util.ToastFactory
import com.example.wolmoapp.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    @Inject
    internal lateinit var toastFactory: ToastFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onResume() {
        super.onResume()
        toastFactory.show("Example of injecting toastFactory!")
    }

}