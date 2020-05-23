package com.audia.taller_3.ui.tracks

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.audia.taller_3.Adapters.TracksListAdapter
import com.audia.taller_3.Adapters.TracksListAdapterFB
import com.audia.taller_3.DataBase.TracksViewModel
import com.audia.taller_3.DataBaseFireStore.TracksDaoFB
import com.audia.taller_3.DataBaseFireStore.TracksFB
import com.audia.taller_3.DataBaseFireStore.TracksViewModelFB
import com.audia.taller_3.R
import com.google.firebase.firestore.FirebaseFirestore

class TracksFragment : Fragment() {

    private lateinit var mView: View
    private lateinit var tracksViewModel: TracksViewModel
    private lateinit var tracks_list_view: RecyclerView

    private lateinit var tracksDaoFB: TracksDaoFB
    private lateinit var firestore: FirebaseFirestore
    private lateinit var tracksViewModelFB: TracksViewModelFB

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        mView = inflater.inflate(R.layout.fragment_tracks, container, false)

        tracksViewModel = activity?.run {
            ViewModelProvider(this).get(TracksViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        tracksViewModelFB = activity?.run {
            ViewModelProvider(this).get(TracksViewModelFB::class.java)
        } ?: throw Exception("Invalid Activity")

        firestore= FirebaseFirestore.getInstance()
        tracksDaoFB= TracksDaoFB(firestore)
        addObserver()

        return mView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tracklist, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_export-> {exportToFirestore()}
            R.id.menu_delete-> {tracksViewModelFB.deleteAll()}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun exportToFirestore() {
        val tracks= arrayOf(
            TracksFB(
                0,
                "a25_8_yhlqmdlg",
                "25/8",
                "Bad Bunny",
                "YHLQMDLG",
                "TRAP",
                240,
                "a25_8_yhlqmdlg.mp3",
                "album_yhlqmdlg"),

            TracksFB(
                1,
                "amarillo_colores",
                "Amarillo",
                "J Balvin",
                "Colores",
                "reggaeton",
                13,
                "amarillo_colores.mp3",
                "album_colores"),

            TracksFB(
                2,
                "arabella_am",
                "Arabella",
                "Arctic Monkeys",
                "AM",
                "Alternative Rock",
                251,
                "arabella_am.mp3",
                "album_am"),

            TracksFB(
                3,
                "arcoiris_colores",
                "Arcoiris",
                "J Balvin",
                "Colores",
                "reggaeton",
                13,
                "arcoiris_colores.mp3",
                "album_colores"),

            TracksFB(
                4,
                "azul_colores",
                "Azul",
                "J Balvin",
                "Colores",
                "reggaeton",
                13,
                "azul_colores.mp3",
                "album_colores"),

            TracksFB(
                5,
                "bichiyal_yhlqmdlg",
                "Bichiyal",
                "Bad Bunny",
                "YHLQMDLG",
                "TRAP",
                266,
                "bichiyal_yhlqmdlg.mp3",
                "album_yhlqmdlg"),

            TracksFB(
                6,
                "blanco_colores",
                "Blanco",
                "J Balvin",
                "Colores",
                "reggaeton",
                13,
                "blanco_colores.mp3",
                "album_colores"),

            TracksFB(
                7,
                "cancion_rosa",
                "Canción Rosa",
                "La Toma",
                "¿Que pasa en casa?",
                "Alternative Rock",
                251,
                "cancion_rosa.mp3",
                "album_la_toma"),

            TracksFB(
                8,
                "do_i_wanna_know_am",
                "Do i wanna know?",
                "Arctic Monkeys",
                "AM",
                "Alternative Rock",
                265,
                "do_i_wanna_know_am.mp3",
                "album_am"),

            TracksFB(
                9,
                "fireside_am",
                "Fireside",
                "Arctic Monkeys",
                "AM",
                "Alternative Rock",
                181,
                "fireside_am.mp3",
                "album_am"),

            TracksFB(
                10,
                "gris_colores",
                "Gris",
                "J Balvin",
                "Colores",
                "reggaeton",
                13,
                "gris_colores.mp3",
                "album_colores"),

            TracksFB(
                11,
                "i_wanna_be_yours_am",
                "I wanna be yours",
                "Arctic Monkeys",
                "AM",
                "Alternative Rock",
                184,
                "i_wanna_be_yours_am.mp3",
                "album_am"),

            TracksFB(
                12,
                "hablamos_manana_yhlqmdlg",
                "Hablamos mañana",
                "Bad Bunny",
                "YHLQMDLG",
                "TRAP",
                266,
                "hablamos_manana_yhlqmdlg.mp3",
                "album_yhlqmdlg"),

            TracksFB(
                13,
                "i_want_it_all_am",
                "I want it all",
                "Arctic Monkeys",
                "AM",
                "Alternative Rock",
                185,
                "i_want_it_all_am.mp3",
                "album_am"),

            TracksFB(
                14,
                "ignorantes_yhlqmdlg",
                "Ignorantes",
                "Bad Bunny",
                "YHLQMDLG",
                "TRAP",
                247,
                "ignorantes_yhlqmdlg.mp3",
                "album_yhlqmdlg"),

            TracksFB(
                15,
                "knee_socks_am",
                "Knee socks",
                "Arctic Monkeys",
                "AM",
                "Alternative Rock",
                257,
                "knee_socks_am.mp3",
                "album_am"),

            TracksFB(
                16,
                "la_luna",
                "La Luna",
                "La Toma",
                "¿Que pasa en casa?",
                "Alternative Rock",
                218,
                "la_luna.mp3",
                "album_la_toma"),

            TracksFB(
                17,
                "la_dificil_yhlqmdlg",
                "La dificil",
                "Bad Bunny",
                "YHLQMDLG",
                "TRAP",
                192,
                "la_dificil_yhlqmdlg.mp3",
                "album_yhlqmdlg"),

            TracksFB(
                18,
                "mad_sounds_am",
                "Mad sounds",
                "Arctic Monkeys",
                "AM",
                "Alternative Rock",
                215,
                "mad_sounds_am.mp3",
                "album_am"),

            TracksFB(
                19,
                "morado_colores",
                "Morado",
                "J Balvin",
                "Colores",
                "reggaeton",
                13,
                "morado_colores.mp3",
                "album_colores"),

            TracksFB(
                20,
                "negro_colores",
                "Negro",
                "J Balvin",
                "Colores",
                "reggaeton",
                13,
                "negro_colores.mp3",
                "album_colores"),

            TracksFB(
                21,
                "no_1_party_anthem_am",
                "No.1 party anthem",
                "Arctic Monkeys",
                "AM",
                "Alternative Rock",
                243,
                "no_1_party_anthem_am.mp3",
                "album_am"),

            TracksFB(
                22,
                "one_for_the_road_am",
                "One for the road",
                "Arctic Monkeys",
                "AM",
                "Alternative Rock",
                206,
                "one_for_the_road_am.mp3",
                "album_am"),

            TracksFB(
                23,
                "pero_ya_no_yhlqmdlg",
                "Pero ya no",
                "Bad Bunny",
                "YHLQMDLG",
                "TRAP",
                165,
                "pero_ya_no_yhlqmdlg.mp3",
                "album_yhlqmdlg"),

            TracksFB(
                24,
                "r_u_mine_am",
                "RU mine",
                "Arctic Monkeys",
                "AM",
                "Alternative Rock",
                224,
                "r_u_mine_am.mp3",
                "album_am"),

            TracksFB(
                25,
                "rojo_colores",
                "Rojo",
                "J Balvin",
                "Colores",
                "reggaeton",
                13,
                "rojo_colores.mp3",
                "album_colores"),

            TracksFB(
                26,
                "rosado_colores",
                "Rosado",
                "J Balvin",
                "Colores",
                "reggaeton",
                13,
                "rosado_colores.mp3",
                "album_colores"),

            TracksFB(
                27,
                "safaera_yhlqmdlg",
                "Safaera",
                "Bad Bunny",
                "YHLQMDLG",
                "TRAP",
                295,
                "safaera_yhlqmdlg.mp3",
                "album_yhlqmdlg"),

            TracksFB(
                28,
                "si_veo_a_tu_mama_yhlqmdlg",
                "Si veo a tu mama",
                "Bad Bunny",
                "YHLQMDLG",
                "TRAP",
                265,
                "si_veo_a_tu_mama_yhlqmdlg.mp3",
                "album_yhlqmdlg"),

            TracksFB(
                29,
                "snap_out_of_it_am",
                "Snap out of it",
                "Arctic Monkeys",
                "AM",
                "Alternative Rock",
                247,
                "snap_out_of_it_am.mp3",
                "album_am"),

            TracksFB(
                31,
                "solia_yhlqmdlg",
                "Solia",
                "Bad Bunny",
                "YHLQMDLG",
                "TRAP",
                159,
                "solia_yhlqmdlg.mp3",
                "album_yhlqmdlg"),

            TracksFB(
                31,
                "verde_colores",
                "Verde",
                "J Balvin",
                "Colores",
                "reggaeton",
                13,
                "verde_colores.mp3",
                "album_colores"),

            TracksFB(
                32,
                "whyd_you_only_call_me_when_youre_high_am",
                "Why'd you only call me when you're high",
                "Arctic Monkeys",
                "AM",
                "Alternative Rock",
                163,
                "whyd_you_only_call_me_when_youre_high_am.mp3",
                "album_am"),

            TracksFB(
                33,
                "yo_perreo_sola_yhlqmdlg",
                "Yo perreo sola",
                "Bad Bunny",
                "YHLQMDLG",
                "TRAP",
                200,
                "yo_perreo_sola_yhlqmdlg.mp3",
                "album_yhlqmdlg")
        )
        tracksViewModelFB.saveTrack(*tracks)
    }

    private fun addObserver() {
        val adapter = context?.let { TracksListAdapterFB(it) }
        tracks_list_view=mView.findViewById(R.id.track_list)
        tracks_list_view.layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        tracks_list_view.adapter = adapter
        tracksViewModelFB.tracksList.observe(viewLifecycleOwner, Observer { tracksList ->
            tracksList?.let {
                if (adapter != null) {
                    adapter.setTracks(it)
                }
            }
        })
    }
}
