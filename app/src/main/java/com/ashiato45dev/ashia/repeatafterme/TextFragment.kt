package com.ashiato45dev.ashia.repeatafterme

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text
import java.util.*


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
    private var text: String = "This is a sample sentence."
    private var listener: OnFragmentInteractionListener? = null
    public lateinit var sp : TextToSpeech ;
    private val SPEECH_REQUEST_CODE = 0
    lateinit var sharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            text = it.getString(ARG_TEXT1)
        }


    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setEnglishToSpeaker(sp: TextToSpeech){
        if(Locale.US in sp.availableLanguages) {
            sp.setLanguage(Locale.ENGLISH)
        }else{
            Toast.makeText(context, "You TTS service does not support English.", Toast.LENGTH_LONG)
            val installIntent = Intent()
            installIntent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
            startActivity(installIntent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_text, container, false)
        val txtMain = view.findViewById<TextView>(R.id.txtMain);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);


        sp = TextToSpeech(context, {
            view?.findViewById<Button>(R.id.btnPlay)?.isEnabled = true;
            setEnglishToSpeaker(sp);


        })


        view.findViewById<Button>(R.id.btnPlay).setOnClickListener{
            setEnglishToSpeaker(sp);
            sp.speak(text, TextToSpeech.QUEUE_FLUSH, null, "test")
        }

        view.findViewById<Button>(R.id.btnRecord).setOnClickListener{
            txtMain.setTextColor(Color.BLACK);
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            // Start the activity, the intent will be populated with the speech text
            startActivityForResult(intent, SPEECH_REQUEST_CODE)
        }

        view.findViewById<Button>(R.id.btnAnswer).setOnClickListener{
            txtMain.setTextColor(Color.RED);
            txtMain.text = text;
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(data != null) {
            if (requestCode == SPEECH_REQUEST_CODE && resultCode ==  Activity.RESULT_OK) {
                val results = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS)
                val spokenText = results[0]


                val acceptance = sharedPref.getString("list_preference_acceptance", "90").toFloat()/100;
                val diff = (calcNormalizedLevenshteinDistance(spokenText, text).toFloat())/text.length;
                val isok = if(diff <= 1 - acceptance) "[OK]" else "[Retry!(%d%%)]".format(((1 - diff)*100).toInt())

                // Do something with spokenText
                this.view?.findViewById<TextView>(R.id.txtMain)?.setText(spokenText + isok)
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("mine", "cancelled!")
            }


            super.onActivityResult(requestCode, resultCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
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
