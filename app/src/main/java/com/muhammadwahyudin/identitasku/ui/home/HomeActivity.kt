package com.muhammadwahyudin.identitasku.ui.home

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.model.DataType
import com.muhammadwahyudin.identitasku.ui._base.BaseActivity
import kotlinx.android.synthetic.main.activity_home.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class HomeActivity : BaseActivity(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: HomeViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initializeUI()
    }

    private fun initializeUI() {
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)

        viewModel.getAllDataType().observe(this, Observer { dataTypes ->
            val stringBuilder = StringBuilder()
            dataTypes.forEach { dataType ->
                stringBuilder.append("$dataType\n\n")
            }
            tv_test.text = stringBuilder.toString()
        })

        btn_test.setOnClickListener {
            val dataType = DataType(et_name_datatype.text.toString(), false, true)
            viewModel.addDataType(dataType)
            et_name_datatype.setText("")
        }

    }
}
