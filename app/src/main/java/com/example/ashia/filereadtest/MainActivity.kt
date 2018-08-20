package com.example.ashia.filereadtest

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.media.MediaScannerConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import java.io.*
import java.util.*



data class TestData(val name: String, val price: Int, val desc: String){

}

public class MainActivity : AppCompatActivity() {



    private fun makeFileData(f: File): Map<String, Any>{
        var res = HashMap<String, Any>();
        res.put("name", f.name);
        res.put("length", f.length().toString());
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
                Toast.makeText(context, data.name, Toast.LENGTH_LONG).show();


                val intent = Intent(context, ReadTextActivity::class.java);
                // https://stackoverflow.com/questions/3689581/calling-startactivity-from-outside-of-an-activity/3689900
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("file", data);
                context.startActivity(intent);

            }
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val lvMenu = findViewById<ListView>(R.id.lvMenu);
//        val menuList = mutableListOf<Map<String, Any>>();
//
//        menuList.add(makeData("Agedofu", 100, "Fried tofu"));
//        menuList.add(makeData("Inari", 200, "Sushi of fried tofu"));
//        menuList.add(makeData("Kinudofu",  150, "Smooth tofu"));
//
//        val from = arrayOf("name", "price");
//        val to = intArrayOf(android.R.id.text1, android.R.id.text2);
//
//        val adapter = SimpleAdapter(this, menuList, android.R.layout.simple_list_item_2, from, to);
//        lvMenu.adapter = adapter;
        updateFileList();
        lvMenu.setOnItemClickListener(ListItemClickListener(this.applicationContext));



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu)
    }

    private fun updateFileList(){
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        val path = dir.resolve("repeat_after_me");
        Toast.makeText(this, path.toString() , Toast.LENGTH_LONG).show();
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
                val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                Toast.makeText(this, dir.toString() + "/" + dir.listFiles()[0].name.toString() , Toast.LENGTH_LONG).show();

            }
            R.id.menuListOptionMakeFile -> {
                val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                val path = File(dir, "test3.txt");
                val fos = FileOutputStream(path);
                fos.write("agepoyo".toByteArray());
                fos.flush();
                fos.close();
                MediaScannerConnection.scanFile(this, arrayOf(path.toString()), null, null);
                Toast.makeText(this, "Made a file." , Toast.LENGTH_LONG).show();
            }
            R.id.menuListOptionUpdate -> {
                updateFileList();

            }

        }
        return super.onOptionsItemSelected(item)
    }
}
