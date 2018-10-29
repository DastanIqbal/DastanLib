package com.dastanapps.dastanlib.chat.base

import com.dastanapps.dastanlib.DastanLibApp
import com.dastanapps.dastanlib.chat.service.XMPPService

/**
 * Created by DastanIqbal on 7/10/16.
 */

open class ChatBase {
    protected var xmppService: XMPPService? = XMPPService.getXMPPService()
    protected var dastanXMPP = XMPPService.getDastanXmppConfig()

    protected fun ensureConnection() {
        if (xmppService != null) {
            xmppService?.start()
        } else {
            XMPPService.runService(DastanLibApp.INSTANCE)
        }
    }
}
