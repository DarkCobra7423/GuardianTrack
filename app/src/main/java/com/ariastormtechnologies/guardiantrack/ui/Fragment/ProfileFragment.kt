package com.ariastormtechnologies.guardiantrack.ui.Fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.WebView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ariastormtechnologies.guardiantrack.R
import com.ariastormtechnologies.guardiantrack.ui.WebFormInterface
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.*

class ProfileFragment : Fragment() {

    companion object {
        var GLOBAL_USER_ID: String? = "687c6679886044c43b1483ea" // Reemplaza por un userId real
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val webView: WebView = view.findViewById(R.id.myinformation)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.addJavascriptInterface(WebFormInterface(requireContext()), "Android")
        webView.loadUrl("file:///android_asset/myinformation.html") // O URL remota si usas Codepen exportado



        // 1. Configurar Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))  // este viene del google-services.json
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        auth = FirebaseAuth.getInstance()

        // 2. Referencia al botón en tu XML
        val signInButton: SignInButton = view.findViewById(R.id.btnGoogleSignIn)
        signInButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val userInfoTextView = view.findViewById<TextView>(R.id.txtUserInfo)

        if (user != null) {
            userInfoTextView.text = "Ya estás logueado como:\n${user.displayName}\n${user.email}"
        } else {
            userInfoTextView.text = "No has iniciado sesión aún"
        }


        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.e("GOOGLE_SIGN_IN", "Fallo el login", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(requireContext(), "Bienvenido ${user?.displayName}", Toast.LENGTH_SHORT).show()
                    view?.findViewById<TextView>(R.id.txtUserInfo)?.text = "Hola, ${user?.displayName}\nEmail: ${user?.email}"
                    Log.d("GOOGLE_AUTH", "Login exitoso: UID ${user?.uid}")
                }
                else {
                    Log.e("GOOGLE_AUTH", "Fallo al autenticar con Firebase", task.exception)
                }
            }

        val user = auth.currentUser
        view?.findViewById<TextView>(R.id.txtUserInfo)?.text = "Hola, ${user?.displayName}\nEmail: ${user?.email}"

    }

}
