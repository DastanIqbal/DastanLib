package com.dastanapps.dastanlib.chat

import android.util.Log
import com.dastanapps.dastanlib.chat.base.ChatBase
import com.dastanapps.dastanlib.log.Logger
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.StanzaListener
import org.jivesoftware.smack.XMPPException
import org.jivesoftware.smack.filter.StanzaTypeFilter
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.roster.RosterEntry
import org.jivesoftware.smack.roster.RosterListener

/**
 * Created by DastanIqbal on 7/10/16.
 */

class XMPPContacts : ChatBase() {

    private val TAG = XMPPContacts::class.java.simpleName
    private val subscriptListener = StanzaListener { packet ->
        Log.d(TAG, packet.toString())
        val presence = packet as Presence
        val newEntry = roster.getEntry(presence.from)
        if (presence.type == Presence.Type.subscribe) {
            Log.d(TAG, "Subscribe Request From:" + packet.getFrom())

            if (newEntry == null) {
                //Send Subscribe
                val subscribe = Presence(Presence.Type.subscribe)
                subscribe.mode = Presence.Mode.available
                subscribe.to = packet.getFrom()
                dastanXMPP.getXmpptcpConnection()!!.sendStanza(subscribe)
                try {
                    roster.createEntry(presence.from, "", null)
                } catch (e: SmackException.NotLoggedInException) {
                    e.printStackTrace()
                } catch (e: SmackException.NoResponseException) {
                    e.printStackTrace()
                } catch (e: XMPPException.XMPPErrorException) {
                    e.printStackTrace()
                }

                val subscribed = Presence(Presence.Type.subscribed)
                subscribed.to = packet.getFrom()
                subscribed.mode = Presence.Mode.available
                dastanXMPP.getXmpptcpConnection()!!.sendStanza(subscribed)
            }

        } else if (presence.type == Presence.Type.subscribed) {
            Log.d(TAG, "Subscribed User Accepted Request From :" + packet.getFrom())
            //Send Subscribed
            val subscribed = Presence(Presence.Type.subscribe)
            subscribed.mode = Presence.Mode.available
            subscribed.to = packet.getFrom()
            dastanXMPP.getXmpptcpConnection()!!.sendStanza(subscribed)
        }
    }
    private val rosterListener = object : RosterListener {
        override fun entriesAdded(addresses: Collection<String>) {
            Log.d(TAG, "inside entriesAdded")
            for (rosterEntry in addresses) {
                Log.d(TAG, "Inside Entries Added: $rosterEntry")
            }
        }

        override fun entriesUpdated(addresses: Collection<String>) {
            Log.d(TAG, "inside entriesUpdated")
        }

        override fun entriesDeleted(addresses: Collection<String>) {
            Log.d(TAG, "inside entriesDeleted")
        }

        override fun presenceChanged(presence: Presence) {
            Log.d(TAG, "inside presenceChanged" + presence.isAvailable)
        }
    }
    private val roster: Roster


    //    public Roster getRoster(){
    //        //No need to intialize
    //        return  roster;
    //    }

    val contacts: Collection<RosterEntry>
        get() {
            for (rosterEntry in roster.entries) {
                Log.d(TAG, rosterEntry.user)
                val presence = roster.getPresence(rosterEntry.user)

                val type = presence.type
                if (presence.isAvailable) {
                    Logger.d(TAG, "Available " + type.toString())
                } else {
                    Logger.d(TAG, "Not Available " + type.toString())
                }
            }
            return roster.entries
        }

    init {
        Log.d(TAG, "XMPPContacts Initialize")
        roster = Roster.getInstanceFor(dastanXMPP.getXmpptcpConnection())
        //roster.setSubscriptionMode(Roster.SubscriptionMode.manual);
        Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.accept_all)
        roster.addRosterListener(rosterListener)
        contacts
        dastanXMPP.getXmpptcpConnection()!!.addAsyncStanzaListener(subscriptListener, StanzaTypeFilter(Presence::class.java))
    }

    fun requestFriend(accountId: String, name: String) {
        try {
            roster.createEntry(accountId, name, null)
            val subscribe = Presence(Presence.Type.subscribed)
            subscribe.mode = Presence.Mode.available
            subscribe.to = accountId
            dastanXMPP.getXmpptcpConnection()!!.sendStanza(subscribe)
        } catch (e: SmackException.NotLoggedInException) {
            e.printStackTrace()
        } catch (e: SmackException.NoResponseException) {
            e.printStackTrace()
        } catch (e: XMPPException.XMPPErrorException) {
            e.printStackTrace()
        } catch (e: SmackException.NotConnectedException) {
            e.printStackTrace()
        }

    }

    fun removeFriend(accoundId: String) {
        for (rosterEntry in contacts) {
            if (rosterEntry.user == accoundId) {
                try {
                    roster.removeEntry(rosterEntry)
                } catch (e: SmackException.NotLoggedInException) {
                    e.printStackTrace()
                } catch (e: SmackException.NoResponseException) {
                    e.printStackTrace()
                } catch (e: XMPPException.XMPPErrorException) {
                    e.printStackTrace()
                } catch (e: SmackException.NotConnectedException) {
                    e.printStackTrace()
                }

            }
        }
    }

    fun getPresence(accountId: String): Int {
        val availability = roster.getPresence(DChatXMPP.getJID(accountId))
        val userMode = availability.mode

        var userState = 0
        /** 0 for offline, 1 for online, 2 for away,3 for busy */
        if (userMode == Presence.Mode.dnd) {
            userState = 3
        } else if (userMode == Presence.Mode.away || userMode == Presence.Mode.xa) {
            userState = 2
        } else if (availability.isAvailable) {
            userState = 1
        }
        return userState
    }
}

