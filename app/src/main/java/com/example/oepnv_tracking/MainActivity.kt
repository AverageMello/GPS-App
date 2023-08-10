package com.example.oepnv_tracking

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.net.URL
import java.util.Random


class MainActivity : AppCompatActivity() {

    val gpshandler = GPSPosition(this)
    private var prototypeStartButton: Button? = null
    private var prototypeStopButton: Button? = null
    private var prototypeStatus: TextView? = null
    private var prototypeReturncode: TextView? = null
    private var prototypeServerIP: TextView? = null
    private var prototypeserverstatus: TextView? = null
    lateinit var prototypelon: TextView
    lateinit var prototypelat: TextView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.prototype_main)
        val preferences = getSharedPreferences("Options", Context.MODE_PRIVATE)
        val IP = preferences.getString("IP", "TBD")

        prototypeStartButton = findViewById(R.id.prototypestartbutton)
        prototypeStopButton = findViewById(R.id.prototypestopbutton)
        prototypeStatus = findViewById(R.id.prototypestatus)
        prototypeReturncode = findViewById(R.id.prototypereturncode)
        prototypeServerIP = findViewById(R.id.prototypeserverip)
        prototypeserverstatus = findViewById(R.id.prototypeserverstatus)
        prototypelon = findViewById(R.id.prototypelon)
        prototypelat = findViewById(R.id.prototypelat)

        var connection = URL("$IP:8080/").readText()

        prototypeServerIP?.setText(IP)
        prototypeStatus?.setText("Verbindung wird aufgebaut")

        if (connection != null) {
            prototypeserverstatus?.setText("Verbunden mit Server")
            prototypeReturncode?.setText(connection)
        } else {
            prototypeserverstatus?.setText("Keine Verbindung möglich")
        }

        prototypeStartButton?.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0:View?) {
                //GPS CALL
                gpshandler.starteUebertragen(prototypelon, prototypelat)
            }
        })

        prototypeStopButton?.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0:View?) {
                //GPS CALL
                gpshandler.stoppeUebertragen()
            }
        })

    }

    companion object {
        class OpenURL(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
            init {
                execute()
            }

            override fun doInBackground(vararg params: Void?): Void? {
                handler()
                return null
            }
        }
    }
}