package com.example.hw2_todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import java.io.FileNotFoundException
import java.io.PrintStream
import java.util.*

class MainActivity : AppCompatActivity() {

    /*
    Variable para almacenar la lista de tareas
     */
    private var varListView: ListView? = null

    /*
    List para almacenar las tareas
     */
    private var varToDoList: MutableList<String>? = null

    /*
    Variable para adaptar la lista al ListView
     */
    private var varArrayAdapter: ArrayAdapter<String>? = null

    /*
    Variable para manejar el campo de texto para que el usuario
    ingrese su tarea
     */
    private var varEditText: EditText? = null

    /*
    Boton para que el usuario añada una nueva tarea
     */
    private var varAddButton: Button? = null

    /*
    Variable para manejar los archivos
     */
    var FILE_NAME: String = "ToDoList.txt"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        varListView = findViewById<ListView>(R.id.to_do_list)

        varToDoList = ArrayList()

        varArrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, varToDoList!!)

        varListView!!.adapter = varArrayAdapter

        varEditText = findViewById<EditText>(R.id.edit_text)

        varAddButton = findViewById<Button>(R.id.add_button)

        varAddButton!!.setOnClickListener {
            if (!TextUtils.isEmpty(varEditText!!.text.toString()) && varToDoList != null) {
                varToDoList!!.add(varEditText!!.text.toString())
                varEditText!!.setText("")
                varArrayAdapter!!.notifyDataSetChanged()
            }
        }

        varListView!!.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
            varToDoList!!.removeAt(position)
            varArrayAdapter!!.notifyDataSetChanged()
            true
        }

    }

   override fun onResume() {
        super.onResume()
        if (varToDoList != null) {
            try {
                val scanner = Scanner(openFileInput(FILE_NAME))
                while (scanner.hasNext()) {
                    val line = scanner.nextLine()
                    varToDoList!!.add(line)
                }
                scanner.close()
                varArrayAdapter!!.notifyDataSetChanged()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

        }
    }

    override fun onPause() {
        super.onPause()

        try {
            val printStream = PrintStream(openFileOutput(FILE_NAME, MODE_PRIVATE))
            for (i in varToDoList!!.indices) {
                printStream.println(varToDoList!![i])
            }
            printStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

    }

}

