package com.example.c323_project9

import android.hardware.SensorManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import kotlinx.coroutines.launch
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class SelfieViewModel : ViewModel() {
    private val TAG = "SelfieViewModel"
    // initialize Firebase auth and all associated variables such as navigation gets
    private var auth: FirebaseAuth
    var user: FirebaseUser?
    var verifyPassword = ""
    lateinit var accelerometerSensor: MeasurableSensor
    var accelerometerData = floatArrayOf(
        SensorManager.GRAVITY_EARTH, SensorManager.GRAVITY_EARTH, 0.0F
    )
    private val _isShaking = MutableLiveData<Boolean>(false)
    val isShaking: LiveData<Boolean>
        get() = _isShaking
    var selfieId : String = ""
    var selfie = MutableLiveData<Selfie>()
    private val _selfies : MutableLiveData<MutableList<Selfie>> = MutableLiveData()
    val selfies : LiveData<List<Selfie>>
        get() = _selfies as LiveData<List<Selfie>>

    private val _navigateToSelfie = MutableLiveData<String?>()
    val navigateToSelfie: LiveData<String?>
        get() = _navigateToSelfie

    private val _navigateToList = MutableLiveData<Boolean>(false)
    val navigateToList: LiveData<Boolean>
        get() = _navigateToList

    private val _errorHappened = MutableLiveData<String?>()
    val errorHappened: LiveData<String?>
        get() = _errorHappened

    private val _navigateToSignUp = MutableLiveData<Boolean>(false)
    val navigateToSignUp: LiveData<Boolean>
        get() = _navigateToSignUp

    private val _navigateToSignIn = MutableLiveData<Boolean>(false)
    val navigateToSignIn: LiveData<Boolean>
        get() = _navigateToSignIn

    private lateinit var selfiesCollection: StorageReference
    private lateinit var selfiesCollectionDb: DatabaseReference

    // init block to setup firebase Authentication and call the function that initializes database
    init {
        auth = Firebase.auth
        user = getCurrentUser()
        if (selfieId.trim() == "") {
            selfie.value = Selfie()
        }
        _selfies.value = mutableListOf<Selfie>()
        initializeTheDatabaseReference()
    }

    /**
     * initializeTheDatabaseReference()
     * initialize the Firebase Realtime Database and get the reference
     * addValueEventListener is always looking for any changes in the Database and adding notes if needed
     * if there is an error somewhere, it logs the failure message and cancels the post.
     */
    fun initializeTheDatabaseReference() {
        val storage = Firebase.storage
        selfiesCollection = storage.reference
        val database = Firebase.database
        selfiesCollectionDb = database
            .reference

        selfiesCollectionDb.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var selfiesList : ArrayList<Selfie> = ArrayList()
                for (selfieSnapshot in dataSnapshot.children) {
                    // TODO: handle the post
                    var selfie = selfieSnapshot.getValue<Selfie>()
                    selfie?.selfieId = selfieSnapshot.key!!
                    selfiesList.add(selfie!!)
                }
                _selfies.value = selfiesList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.v("FAIL", "This Post has Failed.")
            }
        })

    }


    // when note is clicked, navigate to the Edit Note screen
    fun onSelfieClicked(selectedNote: Selfie) {
        _navigateToSelfie.value = selectedNote.selfieId
        selfieId = selectedNote.selfieId
    }


    // after note is navigated, make its value null
    fun onSelfieNavigated() {
        _navigateToSelfie.value = null
    }

    // after user has navigated back to home screen, make _navigateToList false
    fun onNavigatedToList() {
        _navigateToList.value = false
    }

    // when user navigates to sign up page, make _navigateToSignUp true
    fun navigateToSignUp() {
        _navigateToSignUp.value = true
    }

    // after user has navigated to sign up page, make _navigateToSignUp false
    fun onNavigatedToSignUp() {
        _navigateToSignUp.value = false
    }

    // when user navigates to sign in page, make _navigateToSignIn true
    fun navigateToSignIn() {
        _navigateToSignIn.value = true
    }

    // after user has navigates to sign in page, make _navigateToSignIn false
    fun onNavigatedToSignIn() {
        _navigateToSignIn.value = false
    }

    // after user has navigates to sign in page, make _navigateToSignIn false
    fun onNavigatedToTakePhoto() {
        _isShaking.value = false
    }

    /**
     * signIn()
     *
     * if email and password fields are empty, return _errorHappened
     * else
     * sign in with email/pw using in-built Firebase function
     */
    fun signIn(email : String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _errorHappened.value = "Email and password cannot be empty."
            return
        }
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                initializeTheDatabaseReference()
                _navigateToList.value = true
            } else {
                _errorHappened.value = it.exception?.message
            }
        }
    }

    /**
     * signUp()
     *
     * if email and password fields are empty or password and verify don't match, return _errorHappened
     * else
     * create user with email/pw using in-built Firebase function
     */
    fun signUp(email : String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _errorHappened.value = "Email and password cannot be empty."
            return
        }
        if (password != verifyPassword) {
            _errorHappened.value = "Password and verify do not match."
            return
        }
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _navigateToSignIn.value = true
            } else {
                _errorHappened.value = it.exception?.message
            }
        }
    }

    // get current user from Firebase
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun initializeSensor(sAccelerometer: MeasurableSensor) {

        //initialize accelerometer sensor
        accelerometerSensor = sAccelerometer
        accelerometerSensor.startListening()
        accelerometerSensor.setOnSensorValuesChangedListener { a ->
            val x: Float = a[0]
            val y: Float = a[1]
            val z: Float = a[2]
            accelerometerData[1] = accelerometerData[0]
            accelerometerData[0] = Math.sqrt((x * x).toDouble() + y * y + z * z).toFloat()
            val delta: Float = accelerometerData[0] - accelerometerData[1]
            accelerometerData[2] = accelerometerData[2] * 0.9f + delta
            if (accelerometerData[2] > 1) {
                _isShaking.value = true
            }
        }
    }
}