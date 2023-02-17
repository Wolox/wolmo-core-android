package ar.com.wolox.wolmo.wolmoapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import androidx.appcompat.app.AppCompatActivity
import ar.com.wolox.wolmo.core.filesystem.PlainFileFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pff = PlainFileFactory()
        pff.createWithWriter(
            this,
            {
                it.write(
                    ("Overwritten at ${System.currentTimeMillis()}\n")
                        .toByteArray()
                )
            },
            {
                it.putExtra(Intent.EXTRA_TITLE, "invoice.txt")
                it.putExtra(DocumentsContract.EXTRA_INITIAL_URI, Uri.EMPTY)
            }
        )
    }

}
