package com.abx.cellsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import cellsystem.R
import cellsystem.databinding.ActivityListBinding
import com.google.android.material.snackbar.Snackbar

class ListActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivityListBinding
    private lateinit var celularAdapter: CelularAdapter
    private lateinit var database: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.btnAddCelular.setOnClickListener {
            val intent = Intent(this, CelularFormActivity::class.java)
            startActivity(intent)
        }

        database = DatabaseHelper(this)

        celularAdapter = CelularAdapter(mutableListOf(), this)
        binding.rvCelulares.apply {
            layoutManager = LinearLayoutManager(this@ListActivity)
            adapter = celularAdapter
        }
    }

    override fun onResume() {
        getData()
        super.onResume()
    }

    fun launchIntent(intent: Intent) {
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, getString(R.string.profile_error_no_resolve), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun getData() {
        /*val data = mutableListOf(
            Celular(1,"352161436412345","913701849")
        )*/
        val data = database.getAllCelular()
        data.forEach { celular ->
            deleteCelularAuto(celular)
        }
        data.forEach { celular ->
            addCelularAuto(celular)
        }
    }

    private fun addCelularAuto(celular: Celular) {
        celularAdapter.add(celular)
    }

    private fun deleteCelularAuto(celular: Celular) {
        celularAdapter.remove(celular)
    }

    //Cuando se deja presionado
    override fun onLongClick(celular: Celular, currentAdapter: CelularAdapter) {
        val builder = AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_title_delete))
            .setPositiveButton(getString(R.string.dialog_ok), { dialogInterface, i ->
                if (database.deleteCelular(celular)) {
                    currentAdapter.remove(celular)
                    showMessage(R.string.message_write_database_success)
                } else {
                    showMessage(R.string.message_write_database_error)
                }
            })
            .setNegativeButton(getString(R.string.dialog_cancel), null)
        builder.create().show()
    }

    override fun onClickCelNumber(intent: Intent) {
        Log.i("Valor de intentActivity", intent.toString())
        Toast.makeText(this, intent.toString(), Toast.LENGTH_SHORT).show()

        launchIntent(intent)
    }

    private fun showMessage(msgRes: Int) {
        Snackbar.make(binding.root, getString(msgRes), Snackbar.LENGTH_SHORT).show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val id = data?.getStringExtra("k_id")
            val nroImei = data?.getStringExtra(getString(R.string.key_nroimei))
            val nroCelular = data?.getStringExtra(getString(R.string.key_nrocel))

            val celular = Celular(id.toString().toLong(), nroImei.toString(), nroCelular.toString())
            addCelularAuto(celular)

        }
    }

}
