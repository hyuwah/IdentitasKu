@file:SuppressLint("RestrictedApi")
package com.muhammadwahyudin.identitasku.biometric

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.ui._views.BiometricDialogV23
import com.muhammadwahyudin.identitasku.utils.Commons
import java.io.IOException
import java.security.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey
import javax.security.cert.CertificateException

@Suppress("DEPRECATION")
@TargetApi(Build.VERSION_CODES.M)
open class BiometricManagerV23 {

    private lateinit var cipher: Cipher
    private lateinit var keyStore: KeyStore
    private lateinit var keyGenerator: KeyGenerator
    private lateinit var cryptoObject: FingerprintManagerCompat.CryptoObject

    protected var context: Context? = null

    protected lateinit var title: String
    protected lateinit var subtitle: String
    protected lateinit var description: String
    protected lateinit var negativeButtonText: String
    private lateinit var biometricDialogV23: BiometricDialogV23

    fun displayBiometricPromptV23(biometricCallback: BiometricCallback) {
        generateKey()

        if (initCipher()) {
            cryptoObject = FingerprintManagerCompat.CryptoObject(cipher)
            val fingerprintManagerCompat = FingerprintManagerCompat.from(context!!)
            fingerprintManagerCompat.authenticate(
                cryptoObject, 0, CancellationSignal(),
                object : FingerprintManagerCompat.AuthenticationCallback() {
                    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence) {
                        super.onAuthenticationError(errMsgId, errString)
                        updateStatus(errString.toString())
                        biometricCallback.onAuthenticationError(errMsgId, errString)
                    }

                    override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence) {
                        super.onAuthenticationHelp(helpMsgId, helpString)
                        updateStatus(helpString.toString())
                        biometricCallback.onAuthenticationHelp(helpMsgId, helpString)
                    }

                    override fun onAuthenticationSucceeded(
                        result: FingerprintManagerCompat.AuthenticationResult
                    ) {
                        super.onAuthenticationSucceeded(result)
                        dismissDialog()
                        biometricCallback.onAuthenticationSuccessful()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        updateStatus(context!!.getString(R.string.biometric_failed))
                        biometricCallback.onAuthenticationFailed()
                    }
                },
                null
            )

            displayBiometricDialog(biometricCallback)
        }
    }

    private fun displayBiometricDialog(biometricCallback: BiometricCallback) {
        biometricDialogV23 = BiometricDialogV23(context!!, biometricCallback)
        biometricDialogV23.setTitle(title)
        biometricDialogV23.setSubtitle(subtitle)
        biometricDialogV23.setDescription(description)
        biometricDialogV23.setButtonText(negativeButtonText)
        biometricDialogV23.show()
    }

    private fun dismissDialog() {
        biometricDialogV23.dismiss()
    }

    private fun updateStatus(status: String) {
        biometricDialogV23.updateStatus(status)
    }

    private fun generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)
            keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                "AndroidKeyStore"
            )
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build()
            )

            keyGenerator.generateKey()
        } catch (exc: KeyStoreException) {
            exc.printStackTrace()
        } catch (exc: NoSuchAlgorithmException) {
            exc.printStackTrace()
        } catch (exc: NoSuchProviderException) {
            exc.printStackTrace()
        } catch (exc: InvalidAlgorithmParameterException) {
            exc.printStackTrace()
        } catch (exc: CertificateException) {
            exc.printStackTrace()
        } catch (exc: IOException) {
            exc.printStackTrace()
        }
    }

    private fun initCipher(): Boolean {
        try {
            cipher = Cipher.getInstance(
                KeyProperties.KEY_ALGORITHM_AES + "/" +
                        KeyProperties.BLOCK_MODE_CBC + "/" +
                        KeyProperties.ENCRYPTION_PADDING_PKCS7
            )
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to get Cipher", e)
        } catch (e: NoSuchPaddingException) {
            throw RuntimeException("Failed to get Cipher", e)
        }

        try {
            keyStore.load(null)
            val key = keyStore.getKey(KEY_NAME, null) as SecretKey
            cipher.init(Cipher.ENCRYPT_MODE, key)
            return true
        } catch (e: KeyPermanentlyInvalidatedException) {
            return false
        } catch (e: KeyStoreException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: CertificateException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: UnrecoverableKeyException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: IOException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException("Failed to init Cipher", e)
        }
    }

    companion object {
        private val KEY_NAME = Commons.getUUID()
    }
}