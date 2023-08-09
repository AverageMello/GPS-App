package com.example.oepnv_tracking

import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class GPSPosition {

    //Initialisieren von notwendigen Values bzw. Variables
    private val looperThread = LooperProzess()
    private var fusedLocationProviderClient: FusedLocationProviderClient
    private var sollUebertragen: Boolean = false

    init {
        //Starte den unendlichen Looper Thread
        looperThread.start()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity())
    }

    fun starteUebertragen(test: TextView, test1: TextView) {
        //Setze den Übertragen Wert auf true -> Die Übertragung kann starten
        sollUebertragen = true

        //Der Handler übergibt eine Aufgabe an den Looper Thread solange wie sollÜbertragen auf true ist.
        looperThread.handler!!.post {
            while (sollUebertragen){
                //Rufe die Methode positionBestimmen auf und warte anschließend 5 Sekunden
                positionBestimmen(test, test1)
                SystemClock.sleep(5000)
            }
        }
    }


    //Wurde der Stoppen Button geklickt?
    fun stoppeUebertragen() {
        //Setze den Übertragen Wert auf false -> Die Übertragung stoppt.
        sollUebertragen = false
    }

    private fun positionBestimmen(test: TextView, test1: TextView) {
        //Hat die App die Berechtigung auf Positionsdaten zuzugreifen?
        if(ActivityCompat.checkSelfPermission(MainActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED )
        {
            //Wenn keine Bereichtigung gegeben die Berechtigung erfragen
           ActivityCompat.requestPermissions(MainActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
           return
        }

        val request = CurrentLocationRequest.Builder().setMaxUpdateAgeMillis(4000).setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()
        //Eine Aufgabe erstellen, die dann returned, wenn eine Location gefunden wurde. Returned null wenn keine gefunden werden konnte
        val aufgabe = fusedLocationProviderClient.getCurrentLocation(request, CancellationTokenSource().token)

        //Es konnte eine Location gefunden werden
        aufgabe.addOnSuccessListener {
            //Die Location ist nicht null
            if(it != null){
                //Fülle die entsprechenden Felder im UI mit Längen- und Breitengrad
                test.text = it.longitude.toString()
                test1.text = it.latitude.toString()
            }
        }
    }

}

// Die Klasse LooperProzess ist dafür da einen unendlichen Thread zu starten, der die konstante Positionsbestimmung übernehmen kann ohne das UI zu blocken
@Suppress("DEPRECATION")
class LooperProzess : Thread(){
    private var looper: Looper? = null
    var handler: Handler? = null

    // Bei Aufruf des LooperProzesses soll ein unendlicher Thread gestartet werden
    override fun run() {
        Looper.prepare()

        handler = Handler()

        looper = Looper.myLooper()

        Looper.loop()
    }
}