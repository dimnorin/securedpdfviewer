package org.jivesoftware.smack.znannya.track;

public class TimeTrack {
    public enum Type {
        /**
         * Start tracking of time.
         */
        start,

        /**
         * Stop tracking of time.
         */
        stop,

        /**
         * Update server with info, that client continue viewing doc.
         */
        update,

        /**
         * Allow client to view doc.
         */
        allow,
        
        /**
         * Get balance
         */
        get;
    }
}
