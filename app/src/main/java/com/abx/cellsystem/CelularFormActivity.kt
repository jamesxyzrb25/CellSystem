package com.abx.cellsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import cellsystem.R
import cellsystem.databinding.ActivityCelularFormBinding
import com.google.android.material.snackbar.Snackbar

class CelularFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCelularFormBinding
    private lateinit var database: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCelularFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        database = DatabaseHelper(this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_form, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_send) {
            if (validFields()) {
                val imei_number: String = binding.etNroImei.text.toString().trim()
                val cel_number: String = binding.etNroCelular.text.toString().trim()

                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.dialog_title_add))
                builder.setMessage(joinData(imei_number, cel_number))
                builder.setPositiveButton(getString(R.string.dialog_ok_add), { dialogInterface, i ->
                    with(binding) {
                        etNroImei.text?.clear()
                        etNroCelular.text?.clear()
                    }
                })
                builder.setNegativeButton(getString(R.string.dialog_cancel_add), null)
                val dialog: AlertDialog = builder.create()
                dialog.show()
                val celular = Celular(nroImei = imei_number, nroCelular = cel_number)
                celular.id = database.insertCelular(celular)
                if (celular.id != Constants.ID_ERROR) {
                    sendData(celular.id, imei_number, cel_number)
                    showMessage(R.string.message_write_database_success)
                } else {
                    showMessage(R.string.message_write_database_error)
                }

            }
        } else if (item.itemId == android.R.id.home) {
            onBackPressed()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sendData(id: Long, imei_number: String, cel_number: String) {
        val intent = Intent()
        with(binding) {
            intent.apply {
                putExtra(getString(R.string.key_id), id)
                putExtra(getString(R.string.key_nroimei), imei_number)
                putExtra(getString(R.string.key_nrocel), cel_number)
            }
        }
        setResult(RESULT_OK, intent)
    }

    private fun validFields(): Boolean {
        var isValid = true
        //Validar campo imei
        if (binding.etNroImei.text.isNullOrEmpty()) {
            binding.TILNroImei.run {
                error = getString(R.string.help_required)
                requestFocus()
            }
            isValid = false
        } else {
            if (binding.etNroImei.text?.length!! != 15) {
                binding.TILNroImei.run {
                    error = getString(R.string.format_imei_required)
                    requestFocus()
                }
                isValid = false
            } else {
                binding.TILNroImei.error = null
            }

        }
        //Validar campo celular
        if (binding.etNroCelular.text.isNullOrEmpty()) {
            binding.TILNroCelular.run {
                error = getString(R.string.help_required)
                requestFocus()
            }
            isValid = false
        } else {
            if (binding.etNroCelular.text?.length!! != 9) {
                binding.TILNroCelular.run {
                    error = getString(R.string.format_numcel_required)
                    requestFocus()
                }
                isValid = false
            } else {
                binding.TILNroCelular.error = null
            }

        }
        return isValid
    }

    private fun joinData(vararg fields: String): String {
        var result = ""

        fields.forEach { field ->
            if (field.isNotEmpty()) {
                result += "$field\n"
            }

        }

        return result
    }

    private fun showMessage(msgRes: Int) {
        Snackbar.make(binding.root, getString(msgRes), Snackbar.LENGTH_SHORT).show()

    }

}