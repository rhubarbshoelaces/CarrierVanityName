package gay.nullby.carriername

import android.annotation.SuppressLint
import android.app.IActivityManager
import android.app.Instrumentation
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.system.Os
import android.telephony.CarrierConfigManager
import android.util.Log
import org.lsposed.hiddenapibypass.HiddenApiBypass
import rikka.shizuku.ShizukuBinderWrapper
import rikka.shizuku.SystemServiceHelper

class BrokerInstrumentation : Instrumentation() {
    companion object {
        const val TAG = "BrokerInstrumentation"
        const val ARG_SUB_ID = "broker_subId"
        const val ARG_CLEAR = "broker_clear"
        const val ARG_OVERRIDES = "broker_overrides"
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(arguments: Bundle?) {
        super.onCreate(arguments)

        HiddenApiBypass.addHiddenApiExemptions("L")

        if (arguments == null) {
            finish(0, Bundle())
            return
        }

        val subId = arguments.getInt(ARG_SUB_ID, -1)
        val clear = arguments.getBoolean(ARG_CLEAR, false)
        val overrides: PersistableBundle? =
            if (clear) null else arguments.getParcelable(ARG_OVERRIDES)

        val am = IActivityManager.Stub.asInterface(
            ShizukuBinderWrapper(SystemServiceHelper.getSystemService(Context.ACTIVITY_SERVICE))
        )
        am.startDelegateShellPermissionIdentity(Os.getuid(), null)
        try {
            val ccm = context.getSystemService(CarrierConfigManager::class.java)
            try {
                ccm.overrideConfig(subId, overrides, true)
            } catch (e: SecurityException) {
                // Some patched builds restrict persistent=true to system apps.
                if (e.message?.contains("only can be invoked by system app") == true) {
                    ccm.overrideConfig(subId, overrides, false)
                } else {
                    throw e
                }
            }
        } catch (t: Throwable) {
            Log.e(TAG, "Broker override failed", t)
        } finally {
            try {
                am.stopDelegateShellPermissionIdentity()
            } catch (t: Throwable) {
                Log.e(TAG, "stopDelegateShellPermissionIdentity failed", t)
            }
            finish(0, Bundle())
        }
    }
}