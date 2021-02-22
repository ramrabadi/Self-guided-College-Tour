package com.tw.ouguidedtour
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction

class DataActivity : AppCompatActivity() {

    object TourData: Table(){
        val id = integer("id").autoIncrement()
        val locationName = varchar("locationName", 50)


        override val primaryKey = PrimaryKey(id, name = "PK_MapData_ID")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)
        val qr = intent.getStringExtra("QRData")
        val iv: ImageView = findViewById(R.id.picture1)
        val tv: TextView = findViewById(R.id.textView2)
        val str:String = "This is the first floor lab. This lab is open to all students and many classes have lab time here"


        if("3rdfloorlab" == qr){
            iv.setImageResource(R.drawable.lab3rdfloor)
            tv.text = str
            //tv.setT
        }
        val mainMenuButton: Button = findViewById(R.id.mainMenuButton)
        mainMenuButton.setOnClickListener {
            val dataIntent = Intent(this, MainMenuActivity::class.java)
            startActivity(dataIntent)

        }
        /*
        Database.connect("jdbc:sqlserver://neon-camp-293918:us-central1:tw;databaseName=Location", driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver", user = "root", password = "root")
        transaction{
            addLogger(StdOutSqlLogger)
           SchemaUtils.create(TourData)
            val qr = intent.getStringExtra("QRData")
            for(l in TourData.selectAll()){
                if(l[TourData.locationName] == qr){
                    println("")
                }
            }
        }

         */
    }

}