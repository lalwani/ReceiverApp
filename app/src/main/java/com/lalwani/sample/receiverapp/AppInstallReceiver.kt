package com.lalwani.sample.receiverapp

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class AppInstallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Check if the action is for a package being added
        if (Intent.ACTION_PACKAGE_ADDED == intent.action) {
            // Extract the package name of the newly installed app
            val data = intent.data
            val packageName = data?.schemeSpecificPart
            Log.d("yyyy", "AppInstallReceiver. app installed $packageName")

            // Check if the installed app is "App 2" (Sender App)
            if ("com.lalwani.sample.senderapp" == packageName) {
                // Call method to insert data into App 2's ContentProvider
                insertIntoSenderAppContentProvider(context)
            }
        }
        Log.d("yyyy", "AppInstallReceiver invoked")
    }

    private fun insertIntoSenderAppContentProvider(context: Context) {
        // Prepare the data to insert
        val values = ContentValues()
        values.put("package_name", "com.lalwani.sample.senderapp")
        values.put("install_time", System.currentTimeMillis())

        // Define App 2's ContentProvider URI
        val senderAppContentUri = Uri.parse("content://com.lalwani.sample.crossappprovider/data")

        // Insert the data into App 2's ContentProvider
        try {
            val executorService = Executors.newSingleThreadExecutor {
                r -> Thread(r, "InsertIntoSenderAppContentProviderThread")
            }
            executorService.execute {
                Thread.sleep(1000)
                Log.d("yyyy", "AppInstallReceiver. inserting data into Sender App's ContentProvider")
                val uri = context.contentResolver.insert(senderAppContentUri, values)
                Log.d("yyyy", "AppInstallReceiver. data inserted successfully $uri")
            }

        } catch (e: Exception) {
            Log.e("yyyy", "AppInstallReceiver. Failed to insert data: " + e.message)
        }
    }
}

