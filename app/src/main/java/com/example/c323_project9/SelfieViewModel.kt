package com.example.c323_project9

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import kotlinx.coroutines.launch
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class SelfieViewModel : ViewModel() {

    // initialize Firebase auth and all associated variables such as navigation gets
    private var auth: FirebaseAuth
    var user: FirebaseUser?
    var verifyPassword = ""
    var selfieId : String = ""
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

    // init block to setup firebase Authentication and call the function that initializes database
    init {
        auth = Firebase.auth
        user = getCurrentUser()
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
}