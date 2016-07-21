package noz.httpclientnoz;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by CLient-Pc on 21/07/2016.
 */

public class PostDataWithImage {
    private ArrayList<StringParam> stringParams = new ArrayList<StringParam>();
    private ArrayList<FileParam> fileParams = new ArrayList<FileParam>();

    public void addStringParam(String nama_param, String value_param){
        stringParams.add(new StringParam(nama_param, value_param));
    }

    public void addFileParam(String param_name, String path_file){
        fileParams.add(new FileParam(param_name, path_file));
    }

    public JSONObject send(String url){
        return new JSONParser().uploadImage(url, stringParams, fileParams);
    }
}

