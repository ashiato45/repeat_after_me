package com.example.ashia.filereadtest

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_TEXT1 = "TEXT1"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TextFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TextFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class TextFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var text: String? = null
    private var listener: OnFragmentInteractionListener? = null
    public lateinit var sp : TextToSpeech ;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            text = it.getString(ARG_TEXT1)
        }


    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_text, container, false)
        val txtMain = view.findViewById<TextView>(R.id.txtMain);
        txtMain.text = text;

        sp = TextToSpeech(context, {
            view?.findViewById<Button>(R.id.btnPlay)?.isEnabled = true
        })

        view.findViewById<Button>(R.id.btnPlay).setOnClickListener{
            sp.speak(text, TextToSpeech.QUEUE_FLUSH, null, "test")
        }

        return view
    }


    override fun onDestroyView() {
        sp.stop()
        super.onDestroyView()
    }

    override fun onDestroy() {
        sp.stop()
        super.onDestroy()
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//        }
    }

    override fun onDetach() {
        super.onDetach()
        sp.stop()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment TextFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
                TextFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_TEXT1, param1)
                    }
                }
    }
}
