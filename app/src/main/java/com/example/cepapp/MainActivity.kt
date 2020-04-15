package com.example.cepapp

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.IdRes
import android.widget.Button;
import android.app.Activity as Activity;
import android.widget.EditText
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import android.util.Log
import android.app.Dialog;
import android.content.DialogInterface
import android.widget.ArrayAdapter
import android.widget.ListView
import org.json.JSONObject;
import java.util.stream.DoubleStream.builder


class MainActivity : AppCompatActivity() {

    private val TAG = "REQUEST"
    private val context = this;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onClick(R.id.Button, {
            fetchCep()
        })
        onClick(R.id.Limpar, {
            clearListAndInputHandler()
        })
    }

    fun Activity.getCepText(@IdRes view: Int): String {
        val edit = findViewById<EditText>(view);
        val cepText = edit.text.toString();
        return cepText
    }

    fun clearListAndInputHandler(){
        var edit = findViewById<EditText>(R.id.cep);
        edit.setText(null);
        val alert = AlertDialog.Builder(this.context);
        alert.setTitle("Clear List")
        alert.setMessage("Are you sure you wanna clear the list?")
        alert.setNegativeButton("no",null)
        alert.setNeutralButton("Yes",   DialogInterface.OnClickListener({dialog, which ->
            val lista = findViewById<ListView>(R.id.lista)
            val adaptador = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listOf())
            lista.setAdapter(adaptador);
        }))
        alert.show();
    }

    fun Activity.onClick(@IdRes view: Int, onClick: (android.view.View) -> Unit): Unit {
        val button = findViewById<Button>(view);
        button.setOnClickListener() {
            onClick(it);
        }
    }

    fun fetchCep() {
        val cep = getCepText(R.id.cep)
        val queue = Volley.newRequestQueue(this)
        val url = "https://viacep.com.br/ws/${cep}/json/"

        val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener { response ->
            val dialog =  AlertDialog.Builder(this)
            val cepObject = JSONObject(response);
            val cepItems = listOf(
                "Cep:  " + cepObject.getString("cep"),
                "Complemento:  " + cepObject.getString("complemento"),
                "Bairro:  " + cepObject.getString("bairro"),
                "Localidade:  " + cepObject.getString("localidade"),
                "UF:    " + cepObject.getString("uf"),
                "Unidade:   "  + cepObject.getString("unidade"),
                "IBGE:    " + cepObject.getString("ibge"),
                "GIA:   "  + cepObject.getString("gia"))
            val lista = findViewById<ListView>(R.id.lista)
            val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,cepItems)
            lista.setAdapter(adapter);
        }, null)
        queue.add(stringRequest)

    }
}