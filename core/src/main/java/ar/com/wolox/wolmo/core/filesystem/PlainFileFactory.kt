package ar.com.wolox.wolmo.core.filesystem

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import java.io.FileOutputStream

class PlainFileFactory : FileFactory() {

    override fun createWithWriter(
        componentActivity: ComponentActivity,
        outputStreamWriter: (FileOutputStream) -> Unit,
        intentDecorator: (Intent) -> Intent
    ) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            intentDecorator(this)
            type = PLAIN_TEXT
        }
        val activityResultLauncher = componentActivity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                alterDocument(activityResult.data!!.data!!, componentActivity, outputStreamWriter)
            }
        }
        activityResultLauncher.launch(intent)
    }

}
