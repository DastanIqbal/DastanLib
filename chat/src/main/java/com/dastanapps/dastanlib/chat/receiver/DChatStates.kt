package com.dastanapps.dastanlib.chat.receiver

/**
 * Created by DastanIqbal on 20/10/16.
 */

object DChatStates {
    var sConnectionState: ConnectionState? = null
    var sLoggedInState: LoggedInState? = null
    var sMessageState: MessageState? = null

    val loggedInState: LoggedInState
        get() = if (sLoggedInState == null) {
            LoggedInState.LOGGED_OUT
        } else sLoggedInState!!

    val messageState: MessageState
        get() = if (sMessageState == null) {
            MessageState.NONE
        } else sMessageState!!

    fun getsConnectionState(): ConnectionState {
        return if (sConnectionState == null) {
            ConnectionState.DISCONNECTED
        } else sConnectionState!!
    }

    enum class ConnectionState {
        CONNECTED, CONNECTING, DISCONNECTING, DISCONNECTED, ERROR, AUTHENTICATED
    }

    enum class LoggedInState {
        LOGGED_IN, LOGGED_OUT
    }

    enum class MessageState {
        SEND, RECEIVE, NONE
    }
}
