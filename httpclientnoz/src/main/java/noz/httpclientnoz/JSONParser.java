package noz.httpclientnoz;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CLient-Pc on 21/07/2016.
 */
public class JSONParser {

    public String makeHttpRequest(String url, String method, List<NameValuePair> params, ArrayList<NameValuePair> headers) {
        return proceed(url, method, params, headers);
    }

    private String proceed(String url, String method, List<NameValuePair> params, ArrayList<NameValuePair> headers){
        InputStream is;

        String json = "";
        // Making HTTP request
        try {
            HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is established.
            // The default value is zero, that means the timeout is not used.
            int timeoutConnection = 0;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            // Set the default socket timeout (SO_TIMEOUT)
            // in milliseconds which is the timeout for waiting for data.
            int timeoutSocket = 0;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpResponse httpResponse = null;

            if(method.equalsIgnoreCase("POST")){
                HttpPost http = new HttpPost(url);
                http.addHeader("Cache-Control", "no-cache");
                if(headers != null){
                    for(int i=0; i<headers.size(); i++){
                        NameValuePair n = headers.get(i);
                        http.addHeader(n.getName(), n.getValue());
                    }
                }

                Log.d("URL PARAM", "URL PARAM "+url);
                Log.d("POST PARAM", "POST PARAM "+params.toString());

                http.setEntity(new UrlEncodedFormEntity(params));
                httpResponse = httpClient.execute(http);
            }
            else if(method.equalsIgnoreCase("GET")){
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;

                HttpGet http = new HttpGet(url);
                http.addHeader("Cache-Control", "no-cache");
                if(headers != null){
                    for(int i=0; i<headers.size(); i++){
                        NameValuePair n = headers.get(i);
                        http.addHeader(n.getName(), n.getValue());
                    }
                }
                Log.d("GET PARAM", "GET PARAM "+url);

                httpResponse = httpClient.execute(http);
            }
            else if(method.equalsIgnoreCase("PUT")){
                HttpPut http = new HttpPut(url);
                http.addHeader("Cache-Control", "no-cache");
                if(headers != null){
                    for(int i=0; i<headers.size(); i++){
                        NameValuePair n = headers.get(i);
                        http.addHeader(n.getName(), n.getValue());
                    }
                }
                Log.d("URL PARAM", "URL PARAM "+url);
                Log.d("PUT PARAM", "PUT PARAM "+params.toString());

                http.setEntity(new UrlEncodedFormEntity(params));
                httpResponse = httpClient.execute(http);
            }

            if(httpResponse != null) {
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                //is.close();
                json = sb.toString();
                Log.d("aaa", "aaa = " + json);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }

    public JSONObject uploadImage(String url, ArrayList<StringParam> listStringParam, ArrayList<FileParam> listFileParam){
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 0;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 0;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        HttpPost post = new HttpPost(url);
        post.addHeader("Cache-Control", "no-cache");
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        InputStream is;
        JSONObject jObj = null;

        try {
            if(listStringParam != null){
                for(int i=0; i<listStringParam.size(); i++){
                    StringParam p = listStringParam.get(i);
                    reqEntity.addPart(p.nama, new StringBody(p.value));
                }
            }

            if(listFileParam != null){
                for(int i=0; i<listFileParam.size(); i++){
                    FileParam fp = listFileParam.get(i);
                    reqEntity.addPart(fp.param_name, new FileBody(new File(fp.path_file)));
                }
            }

            post.setEntity(reqEntity);

            HttpResponse response = httpClient.execute(post);
            HttpEntity resEntity = response.getEntity();
            is = resEntity.getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            is.close();
            String response_str = sb.toString();
            Log.d("aaa", "aaa = "+response_str);
            jObj = new JSONObject(response_str);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jObj;
    }

}