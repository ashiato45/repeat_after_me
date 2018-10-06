package com.ashiato45dev.ashia.repeatafterme

import android.Manifest
import android.app.Dialog
import android.app.DialogFragment
import android.content.*
import android.content.pm.PackageManager
import android.content.res.Resources
import android.media.MediaScannerConnection
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import java.io.*
import java.util.*
import android.preference.PreferenceManager
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import java.nio.charset.Charset


data class TestData(val name: String, val price: Int, val desc: String){

}



public class MainActivity : AppCompatActivity() {

    val MY_PERMISSIONS = 0;


    private fun makeFileData(f: File): Map<String, Any>{
        var res = HashMap<String, Any>();
        res.put("name", f.name);
        res.put("length", f.length().toString() + " bytes");
        res.put("data", f);
        return res;




    }

    protected class ListItemClickListener(val context: Context): AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (parent == null){
                Toast.makeText(context,"no", Toast.LENGTH_LONG).show();
            }else {
                val item = parent.getItemAtPosition(position) as Map<String, Any>;
                val data = item["data"] as File;
                //Toast.makeText(context, data.name, Toast.LENGTH_LONG).show();


                val intent = Intent(context, ReadTextActivity::class.java);
                // https://stackoverflow.com/questions/3689581/calling-startactivity-from-outside-of-an-activity/3689900
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("filepath", data.absolutePath);
                context.startActivity(intent);

            }
        }

    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Here, thisActivity is the current activity
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS);

        }else {
            myinit();
        }

    }

    fun myinit(){
        val lvMenu = findViewById<ListView>(R.id.lvMenu);



        initAndupdateFileList()
        lvMenu.setOnItemClickListener(ListItemClickListener(this.applicationContext));

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    myinit()

                } else {
                    Toast.makeText(this,"Failed at getting the permission.", Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    this.finish()
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu)
    }

    private fun initAndupdateFileList(){
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        val path = dir.resolve(resources.getString(R.string.dirname));

        if(!path.exists()){

            // OK button pressed
            path.mkdirs()
            val sample = File(path, resources.getString(R.string.samplefile_filename));
            val fos = FileOutputStream(sample);
            fos.write(resources.getString(R.string.samplefile_content).toByteArray());
            fos.flush();
            fos.close();
            MediaScannerConnection.scanFile(applicationContext, arrayOf(sample.absolutePath), null, null);
            updateFileList(path);
            Toast.makeText(applicationContext, R.string.dialog_init_made, Toast.LENGTH_LONG)



        }else {
           updateFileList(path);
        }
    }

    private fun updateFileList(path: File){
        val menuList = mutableListOf<Map<String, Any>>();

        for (f in path.listFiles()) {
            if (f.extension == "txt") {
                menuList.add(makeFileData(f));
            }
        }

        val lvMenu = findViewById<ListView>(R.id.lvMenu);
        val from = arrayOf("name", "length");
        val to = intArrayOf(android.R.id.text1, android.R.id.text2);

        val adapter = SimpleAdapter(this, menuList, android.R.layout.simple_list_item_2, from, to);
        lvMenu.adapter = adapter;
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menuListOptionHello -> {
                Toast.makeText(this, "Hello!", Toast.LENGTH_LONG).show();
            }
            R.id.menuListOptionShowPath ->{
                // need to give a permission in AndroidManifest.xml, and in the setting menu of the phone.
                val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                val path = dir.resolve(resources.getString(R.string.dirname));
                Toast.makeText(this, path.absolutePath.toString() , Toast.LENGTH_LONG).show();

            }
            R.id.menuListOptionMakeFile -> {
//                val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//                val path = File(dir, "test3.txt");
//                val fos = FileOutputStream(path);
//                fos.write("agepoyo".toByteArray());
//                fos.flush();
//                fos.close();
//                MediaScannerConnection.scanFile(this, arrayOf(path.toString()), null, null);
//                Toast.makeText(this, "Made a file." , Toast.LENGTH_LONG).show();
            }
            R.id.menuListOptionUpdate -> {
                initAndupdateFileList()

            }
            R.id.menuListOptionSetting -> {
                val intent = Intent(this, SettingsActivity::class.java);

                this.startActivity(intent);
            }

            R.id.menuListOptionEraseSetting ->{
                val sp = PreferenceManager.getDefaultSharedPreferences(this);
                sp.edit().clear().commit();
            }

            R.id.menuListOptionAllInOne -> {
                convertAllInOne();
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun convertAllInOne() {
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        val path = dir.resolve(resources.getString(R.string.dirname));
        val allinonePath = File(path, resources.getString(R.string.allinone_filename));
        if(!allinonePath.exists()){
//            Toast.makeText(this, allinonePath.toString(), Toast.LENGTH_LONG).show();
            Toast.makeText(this, resources.getString(R.string.allinone_notfound), Toast.LENGTH_LONG).show();
            return;
        }
        val reader = allinonePath.bufferedReader(charset = Charset.forName("shift_jis"));
        val sentences = mutableListOf<String>()
        var cnt = 0;
        while(true) {
            val line = reader.readLine() ?: break;
            if (cnt % 4 == 1) {
                sentences.add(line);
            }
            cnt++
        }


        val endOfSections = listOf<Int>(0, 17, 30, 59, 72, 92, 124, 144, 162, 186, 210, 217, 237, 255, 281, 310, 331, 355, 366, 394, 419);

        val chapters = List<List<String>>(endOfSections.size - 1, {i ->
            sentences.subList(endOfSections[i], endOfSections[i + 1])
        })

        var files = mutableListOf<String>()
        chapters.forEachIndexed { index, chapter ->
            val chapterFile = File(path, resources.getString(R.string.allinone_generated) + (index + 1).toString() + ".txt");
            files.add(chapterFile.absolutePath)
            chapterFile.writeText(chapter.joinToString(separator = "\n"))
        }
        files.add(allinonePath.absolutePath)
        allinonePath.delete()
        MediaScannerConnection.scanFile(this, files.toTypedArray(), null, null);



        updateFileList(path)

    }
}
