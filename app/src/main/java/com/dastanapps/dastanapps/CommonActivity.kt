package com.dastanapps.dastanapps

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dastanapps.dastanapps.R.id.*
import com.dastanapps.dastanlib.ChannelB
import com.dastanapps.dastanlib.NotificationB
import com.dastanapps.dastanlib.fragment.TimePickerFragment
import com.dastanapps.dastanlib.utils.*
import kotlinx.android.synthetic.main.activity_common.*
import java.util.*

class CommonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)

        ViewUtils.showToast(this, "Device Resolution:" + DisplayUtils.getDeviceResolution(this))

        tv_datetime.text = DateTimeUtils.currentDateTime()

        btn_pickimage.setOnClickListener {
            CommonUtils.pickImageIntent(this@CommonActivity)
        }

        btn_notification.setOnClickListener {
            val notificationB = NotificationB(
                1030, "This is title", ChannelB(
                    "id_testing", "Testing "
                )
            )
            NotificationUtils.notifyNotification(this, notificationB)
        }

        btn_alarm.setOnClickListener {
            val timePickerFragment = TimePickerFragment()
            timePickerFragment.itimepickerresult = object : TimePickerFragment.ITimePickerResult {
                override fun onTimeSet(time: String, calendar: Calendar, locale: Locale) {
                    val intent = Intent(this@CommonActivity, MainActivity::class.java)
                    val pendingIntent = PendingIntent.getActivity(
                        this@CommonActivity,
                        101,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )

                    AlarmUtils.scheduleExactEvent(this@CommonActivity, calendar, pendingIntent)
                    //AlarmUtils.scheduleExactEvent(this@CommonActivity, 120, pendingIntent)
                    //AlarmUtils.scheduleExactRepeatingEvent(this@CommonActivity, calendar, pendingIntent)
                }
            }
            timePickerFragment.show(supportFragmentManager, "timePicker")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CommonUtils.PICK_IMAGES) {
            data?.data?.run {
                ViewUtils.showToast(this@CommonActivity, this.toString())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CommonUtils.cancelNotificaiton(this, 1030)
    }
}
