package com.audia.taller_3.DataBaseFireStore

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class TracksDaoFB(private val firestore: FirebaseFirestore):TracksDAO {

    private val TAG = "TracksDaoFireStore"
    private var collection:CollectionReference = firestore.collection("TRACKS")
    private var trackList:MutableLiveData<List<TracksFB>> = MutableLiveData()

    override fun getAll(): LiveData<List<TracksFB>> {
        collection.addSnapshotListener { value, exception ->
            if(exception!=null){
                Log.d(TAG,"Error al obtener los datos de firebastore")
                return@addSnapshotListener
            }
            val tracklistTemp: MutableList<TracksFB> = mutableListOf()
            for(doc in value!!){
                tracklistTemp.add(doc.toObject(TracksFB::class.java))
            }
            trackList.value=tracklistTemp
        }
        return trackList
    }

    override fun findByCode(code: Int): TracksFB {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insert(vararg tracks: TracksFB) {
        tracks.forEach { track->
            //collection.document(track.code.toString()).set(track)
            collection.document("%03d".format(track.code)).set(track)
                .addOnSuccessListener {
                    Log.d(TAG,"Track add ${track.code}")
                }
                .addOnFailureListener{
                    Log.d(TAG,"Error add ${track.code}")
                }
        }
    }

    override fun update(track: TracksFB) {
        collection.document(track.code.toString()).set(track)
    }

    override fun delete(track: TracksFB) {
        collection.document(track.code.toString()).delete()
    }

    override fun deleteAll() {
        collection.get()
            .addOnSuccessListener {
                it.documents.forEach{
                    it.reference.delete()
                }
            }
    }

}