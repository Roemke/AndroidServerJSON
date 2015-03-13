package de.zb42.roemke.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import fi.iki.elonen.NanoHTTPD;

/**
 * Created by roemke on 08.03.15.
 */
public class KRNanoServer extends fi.iki.elonen.NanoHTTPD {
    private Context context;
    public KRNanoServer(Context c , int port)
    {
        super(port);
        context = c;
    }
    public KRNanoServer(Context c)
    {
        super(8080);
        context = c;
    }

    @Override public fi.iki.elonen.NanoHTTPD.Response serve(fi.iki.elonen.NanoHTTPD.IHTTPSession session) {
        Map<String, List<String>> decodedQueryParameters =
                decodeParameters(session.getQueryParameterString());

        //get battery status lässt sich hier compilieren, nicht im Webserver
        //liegt an Context, muss mir intent und startservice anschauen
        //hmm, ein Service erbt von Context sollte es also selbst sein, ja, daher uebergebe in KRService
        //die Referenz auf this
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null,ifilter);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        int batteryPct = 0;
        if (scale !=0)
            batteryPct = (int) (level / (float)scale * 100);
        String uri = session.getUri();
        String result=null;
        if (!uri.equals("/getJSON")) {
            StringBuilder sb = new StringBuilder();

            sb.append("<html>");
            sb.append("<head><title>KaRo Server</title></head>");
            sb.append("<body>");
            sb.append("<h1>Debug Server</h1>");

            sb.append("<p><blockquote><b>URI</b> = ").append(
                    String.valueOf(session.getUri())).append("<br />");

            sb.append("<b>Method</b> = ").append(
                    String.valueOf(session.getMethod())).append("</blockquote></p>");

            sb.append("<h3>Headers</h3><p><blockquote>").
                    append(toString(session.getHeaders())).append("</blockquote></p>");

            sb.append("<h3>Parms</h3><p><blockquote>").
                    append(toString(session.getParms())).append("</blockquote></p>");

            sb.append("<h3>Parms (multi values?)</h3><p><blockquote>").
                    append(toString(decodedQueryParameters)).append("</blockquote></p>");

            try {
                Map<String, String> files = new HashMap<String, String>();
                session.parseBody(files);
                sb.append("<h3>Files</h3><p><blockquote>").
                        append(toString(files)).append("</blockquote></p>");
            } catch (Exception e) {
                e.printStackTrace();
            }
            sb.append("<h3>additional infos </h3>");
            sb.append("<p><b>is charging: <b>").append(isCharging).append("</p>");
            sb.append("<p><b>level:  <b>").append(batteryPct).append("</p>");

            sb.append("</body>");
            sb.append("</html>");
            result = sb.toString();
        } //eof !getJSON
        else //request is /getJSON
        {
            JSONObject jo = new JSONObject();
            try {
                jo.put("batteryLevel", level);
                jo.put("batteryIsCharging", isCharging);
            }
            catch (Exception ex) {
                result = "{\"error\":\"true\"}"; //not sure if that will work
            }
            result = jo.toString();
        }
        return new fi.iki.elonen.NanoHTTPD.Response(result);
    }

    private String toString(Map<String, ? extends Object> map) {
        if (map.size() == 0) {
            return "";
        }
        return unsortedList(map);
    }

    private String unsortedList(Map<String, ? extends Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("<ul>");
        for (Map.Entry entry : map.entrySet()) {
            listItem(sb, entry);
        }
        sb.append("</ul>");
        return sb.toString();
    }

    private void listItem(StringBuilder sb, Map.Entry entry) {
        sb.append("<li><code><b>").append(entry.getKey()).
                append("</b> = ").append(entry.getValue()).append("</code></li>");
    }
}
