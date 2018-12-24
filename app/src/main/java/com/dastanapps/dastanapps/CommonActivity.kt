package com.dastanapps.dastanapps

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dastanapps.dastanlib.NotificationB
import com.dastanapps.dastanlib.utils.CommonUtils
import com.dastanapps.dastanlib.utils.DateTimeUtils
import com.dastanapps.dastanlib.utils.DisplayUtils
import com.dastanapps.dastanlib.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_common.*

class CommonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)

        val notificationB = NotificationB()
                .id(1030)
                .channelId("id_testing")
                .channelName("Testing ")
                .title("This is title")
                .desc("This is description")
                //.cancelable(false)
        CommonUtils.openNotification2(this, notificationB)

        ViewUtils.showToast(this, "Device Resolution:" + DisplayUtils.getDeviceResolution(this))

        tv_datetime.text = DateTimeUtils.currentDateTime()

        btn_pickimage.setOnClickListener {
            CommonUtils.pickImageIntent(this@CommonActivity)
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
