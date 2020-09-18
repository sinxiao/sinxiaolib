package ae.sinxiao.android.qrd.core;

import android.content.Intent;

public class BaseEvent extends Intent {
    public static final String EVENT_ID = "event_id";

    public BaseEvent(int eventId) {
        this.putExtra("event_id", eventId);
    }

    public int getEventId() {
        return this.getIntExtra("event_id", 0);
    }
}
