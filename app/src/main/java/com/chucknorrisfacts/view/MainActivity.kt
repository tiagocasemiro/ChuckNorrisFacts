package com.chucknorrisfacts.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.chucknorrisfacts.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        (this as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(android.R.drawable.ic_menu_upload)



        NavigationUI.setupActionBarWithNavController(this, Navigation.findNavController(this, R.id.fragmentHost))
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.fragmentHost).navigateUp()
    }
}
