package gay.nullby.carriername

import android.app.IActivityManager
import android.app.UiAutomationConnection
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.telephony.CarrierConfigManager
import android.telephony.SubscriptionManager
import android.widget.Toast
import rikka.shizuku.ShizukuBinderWrapper
import rikka.shizuku.SystemServiceHelper

class CarrierNameReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Fallback checks for both common Tasker extra string keys
        val newCarrierName = intent.getStringExtra("CARRIER_NAME")
            ?: intent.getStringExtra("new_carrier_name")
            ?: return

        if (newCarrierName.isBlank()) return

        // Grab the default active SIM slot ID dynamically
        val subId = SubscriptionManager.getDefaultSubscriptionId()
        if (subId == SubscriptionManager.INVALID_SUBSCRIPTION_ID) return

        try {
            // Map the bundle parameters exactly how Bleelblep's Broker expects them
            val p = PersistableBundle().apply {
                putBoolean(CarrierConfigManager.KEY_CARRIER_NAME_OVERRIDE_BOOL, true)
                putString(CarrierConfigManager.KEY_CARRIER_NAME_STRING, newCarrierName)
            }

            val am = IActivityManager.Stub.asInterface(
                ShizukuBinderWrapper(SystemServiceHelper.getSystemService(Context.ACTIVITY_SERVICE))
            )

            val args = Bundle().apply {
                putInt(BrokerInstrumentation.ARG_SUB_ID, subId)
                putParcelable(BrokerInstrumentation.ARG_OVERRIDES, p)
            }

            // Route through Bleelblep's Broker Instrumentation structure
            am.startInstrumentation(
                ComponentName(context, BrokerInstrumentation::class.java),
                null,
                8,
                args,
                null,
                UiAutomationConnection(),
                0,
                null,
            )

            Toast.makeText(context, "Tasker automated carrier: $newCarrierName", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Automation routing failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}