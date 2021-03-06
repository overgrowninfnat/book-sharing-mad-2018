package com.example.giuseppedigiorno.booksharing_mad.Controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.giuseppedigiorno.booksharing_mad.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*



class RegisterActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference()
    }

    fun createUserButtonPressed(view: View) {
        var email = registerEmailEditText.text.toString().trim()
        var password = registerPasswordEditText.text.toString().trim()
        var name = registerNameEditText.text.toString().trim()

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(name)){
            createNewAccount(email, password, name)
        }else{
            Toast.makeText(this, getString(R.string.fill_the_fields), Toast.LENGTH_LONG).show()
        }
    }

    fun createNewAccount(email: String, password: String, name: String) {
        mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var currentUser = mAuth!!.currentUser
                        var userId = currentUser!!.uid
                        var userObject = HashMap<String, Any>()
                        userObject.put("name", name)
                        userObject.put("favouriteBooksGeneres", "")
                        userObject.put("bio", "")
                        userObject.put("city", "")
                        userObject.put("photoUrl", "")
                        userObject.put("address", "")
                        userObject.put("latitude", 0.0)
                        userObject.put("longitude", 0.0)
                        userObject.put("totalVote", "0")
                        userObject.put("tokenId", "")
                        userObject.put("sharedBooks", "0")
                        mDatabase!!.child("users").child(userId).setValue(userObject)
                                .addOnCompleteListener { task ->
                                    if(task.isSuccessful){
                                        var showProfileActivity = Intent(this, ShowProfileActivity::class.java)
                                        showProfileActivity.putExtra("name", name)
                                        startActivity(showProfileActivity)
                                    }else{
                                        Toast.makeText(this, getString(R.string.auth_failed),
                                                Toast.LENGTH_SHORT).show()
                                    }
                                }
                    }else{
                        Toast.makeText(this, getString(R.string.registration_alert), Toast.LENGTH_LONG).show()
                    }
                }
    }
}
