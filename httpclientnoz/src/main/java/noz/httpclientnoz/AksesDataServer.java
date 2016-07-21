package noz.httpclientnoz;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CLient-Pc on 21/07/2016.
 */

public class AksesDataServer {

    private List<NameValuePair> params = new ArrayList<>();
    private ArrayList<NameValuePair> headers;

    public void addParam(String nama_param, String value_param) {
        params.add(new BasicNameValuePair(nama_param, value_param));
    }

    public void addHeader(String name, String value){
        if(headers == null) headers = new ArrayList<>();
        headers.add(new BasicNameValuePair(name, value));
    }

    public JSONObject getJSONObjectRespon(String url, String method) {
        JSONObject j = null;
        try {
            j = new JSONObject(getStringRespon(url, method));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j;
    }

    public JSONArray getJSONArrayRespon(String url, String method) {
        JSONArray j = null;
        try {
            j = new JSONArray(getStringRespon(url, method));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j;
    }

    public String getStringRespon(String url, String method) {
        return new JSONParser().makeHttpRequest(url, method, params, headers);
    }

}

