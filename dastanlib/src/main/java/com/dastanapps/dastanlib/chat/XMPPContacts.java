package com.dastanapps.dastanlib.chat;

import android.util.Log;

import com.dastanapps.dastanlib.chat.base.ChatBase;
import com.dastanapps.dastanlib.log.Logger;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;

import java.util.Collection;

/**
 * Created by DastanIqbal on 7/10/16.
 */

public class XMPPContacts extends ChatBase {

    private final String TAG = XMPPContacts.class.getSimpleName();
    private final StanzaListener subscriptListener = new StanzaListener() {
        @Override
        public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
            Log.d(TAG, packet.toString());
            Presence presence = (Presence) packet;
            RosterEntry newEntry = roster.getEntry(presence.getFrom());
            if (presence.getType() == Presence.Type.subscribe) {
                Log.d(TAG, "Subscribe Request From:" + packet.getFrom());

                if (newEntry == null) {
                    //Send Subscribe
                    Presence subscribe = new Presence(Presence.Type.subscribe);
                    subscribe.setMode(Presence.Mode.available);
                    subscribe.setTo(packet.getFrom());
                    dastanXMPP.getXmpptcpConnection().sendStanza(subscribe);
                    try {
                        roster.createEntry(presence.getFrom(), "", null);
                    } catch (SmackException.NotLoggedInException | SmackException.NoResponseException | XMPPException.XMPPErrorException e) {
                        e.printStackTrace();
                    }

                    Presence subscribed = new Presence(Presence.Type.subscribed);
                    subscribed.setTo(packet.getFrom());
                    subscribed.setMode(Presence.Mode.available);
                    dastanXMPP.getXmpptcpConnection().sendStanza(subscribed);
                }

            } else if (presence.getType() == Presence.Type.subscribed) {
                Log.d(TAG, "Subscribed User Accepted Request From :" + packet.getFrom());
                //Send Subscribed
                Presence subscribed = new Presence(Presence.Type.subscribe);
                subscribed.setMode(Presence.Mode.available);
                subscribed.setTo(packet.getFrom());
                dastanXMPP.getXmpptcpConnection().sendStanza(subscribed);
            }
        }
    };
    private final RosterListener rosterListener = new RosterListener() {
        @Override
        public void entriesAdded(Collection<String> addresses) {
            Log.d(TAG, "inside entriesAdded");
            for (String rosterEntry : addresses) {
                Log.d(TAG, "Inside Entries Added: "+rosterEntry);
            }
        }

        @Override
        public void entriesUpdated(Collection<String> addresses) {
            Log.d(TAG, "inside entriesUpdated");
        }

        @Override
        public void entriesDeleted(Collection<String> addresses) {
            Log.d(TAG, "inside entriesDeleted");
        }

        @Override
        public void presenceChanged(Presence presence) {
            Log.d(TAG, "inside presenceChanged" + presence.isAvailable());
        }
    };
    private Roster roster;

    public XMPPContacts() {
        Log.d(TAG, "XMPPContacts Initialize");
        roster = Roster.getInstanceFor(dastanXMPP.getXmpptcpConnection());
        //roster.setSubscriptionMode(Roster.SubscriptionMode.manual);
        Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.accept_all);
        roster.addRosterListener(rosterListener);
        getContacts();
        dastanXMPP.getXmpptcpConnection().addAsyncStanzaListener(subscriptListener, new StanzaTypeFilter(Presence.class));
    }


//    public Roster getRoster(){
//        //No need to intialize
//        return  roster;
//    }

    public Collection<RosterEntry> getContacts() {
        for (RosterEntry rosterEntry : roster.getEntries()) {
            Log.d(TAG, rosterEntry.getUser());
            Presence presence = roster.getPresence(rosterEntry.getUser());

            Presence.Type type = presence.getType();
            if (presence.isAvailable()) {
                Logger.d(TAG, "Available " + type.toString());
            } else {
                Logger.d(TAG, "Not Available " + type.toString());
            }
        }
        return roster.getEntries();
    }

    public void requestFriend(String accountId, String name) {
        try {
            roster.createEntry(accountId, name, null);
            Presence subscribe = new Presence(Presence.Type.subscribed);
            subscribe.setMode(Presence.Mode.available);
            subscribe.setTo(accountId);
            dastanXMPP.getXmpptcpConnection().sendStanza(subscribe);
        } catch (SmackException.NotLoggedInException | SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public void removeFriend(String accoundId) {
        for (RosterEntry rosterEntry : getContacts()) {
            if (rosterEntry.getUser().equals(accoundId)) {
                try {
                    roster.removeEntry(rosterEntry);
                } catch (SmackException.NotLoggedInException | SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getPresence(String accountId) {
        Presence availability = roster.getPresence(DChatXMPP.getJID(accountId));
        Presence.Mode userMode = availability.getMode();

        int userState = 0;
        /** 0 for offline, 1 for online, 2 for away,3 for busy*/
        if (userMode == Presence.Mode.dnd) {
            userState = 3;
        } else if (userMode == Presence.Mode.away || userMode == Presence.Mode.xa) {
            userState = 2;
        } else if (availability.isAvailable()) {
            userState = 1;
        }
        return userState;
    }
}

