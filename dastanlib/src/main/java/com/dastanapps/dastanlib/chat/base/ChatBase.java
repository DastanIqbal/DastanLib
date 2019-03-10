package com.dastanapps.dastanlib.chat.base;

import com.dastanapps.dastanlib.DastanApp;
import com.dastanapps.dastanlib.chat.DChatXMPP;
import com.dastanapps.dastanlib.chat.service.XMPPService;

/**
 * Created by DastanIqbal on 7/10/16.
 */

public class ChatBase {
    protected XMPPService xmppService = XMPPService.getXMPPService();
    protected DChatXMPP dastanXMPP = XMPPService.getDastanXmppConfig();

    protected void ensureConnection() {
        if (xmppService != null) {
            xmppService.start();
        } else {
            XMPPService.runService(DastanApp.getInstance());
        }
    }
}
