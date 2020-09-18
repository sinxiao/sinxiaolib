package ae.sinxiao.android.qrd.model;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

public class QrdScanResult implements Serializable {

    private transient static final String TYPE_URL = "url";
    private transient static final String TYPE_BASE64 = "base64";
    private transient static final String TYPE_TEXT = "text";

    private String scheme;
    private String host;
    private int port;
    private String path;
    private String query;
    private String value;
    private String type;
    private boolean isPayby;
    private boolean isTotok;
    private String pachageName;

    public QrdScanResult(String value) {
        this.value = value;
        parseTheValue(value);
    }

    private static boolean isBase64(String str) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }

    private void parseTheValue(String value) {
        if (value.startsWith("http")) {
            type = TYPE_URL;
            try {
                URI uri = new URI(value);
                scheme = uri.getScheme();
                host = uri.getHost();
                port = uri.getPort();
                path = uri.getPath();
                query = uri.getQuery();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        } else if (isBase64(value)) {
            type = TYPE_BASE64;
        } else {
            type = TYPE_TEXT;
        }
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPayby() {
        return isPayby;
    }

    public void setPayby(boolean payby) {
        isPayby = payby;
    }

    public boolean isTotok() {
        return isTotok;
    }

    public void setTotok(boolean totok) {
        isTotok = totok;
    }

    public String getPachageName() {
        return pachageName;
    }

    public void setPachageName(String pachageName) {
        this.pachageName = pachageName;
    }
}
