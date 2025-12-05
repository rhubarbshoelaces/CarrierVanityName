import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import valid.name.carriername.TargetFragment
import android.telephony.TelephonyManager

class CarrierNameReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val newCarrierName = intent.getStringExtra("CARRIER_NAME") ?: return
        
        // Update the carrier name using the existing method
        val targetFragment = TargetFragment()
        targetFragment.setCarrierName(newCarrierName)

        // Optional: Show a toast message to confirm action
        Toast.makeText(context, "Carrier name set to: $newCarrierName", Toast.LENGTH_SHORT).show()
    }
}
