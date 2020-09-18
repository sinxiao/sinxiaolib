package ae.sinxiao.android.qrd.core;

public class ScanEvent extends BaseEvent {
    public static final int SCAN_RESULT_WEB_HANDLE = 4096;

    public ScanEvent(int eventId) {
        super(eventId);
    }
}
