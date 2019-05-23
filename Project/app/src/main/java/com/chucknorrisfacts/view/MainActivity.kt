package com.chucknorrisfacts.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.chucknorrisfacts.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.chucknorrisfacts.R.layout.activity_main)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
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
