package space.darkowlzz.gsheets2a;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sunny on 04/12/15.
 */
public class GSheets2A {
    String key;
    ArrayList columns;
    DataResult callback;
    Context mCtx;

    public interface DataResult {
        void onReceiveData(JSONObject object);
    }

    public GSheets2A(String key, Context ctx) {
        this.key = key;
        this.mCtx = ctx;
        //this.columns = columns;
    }

    public String getKey() {
        return key;
    }

    public void getData(final DataResult callback) {
        RequestQueue queue = Volley.newRequestQueue(mCtx);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://spreadsheets.google.com/tq?key=" + key,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onReceiveData(processData(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        queue.add(stringRequest);
    }

    protected JSONObject processData(String result) {
        int start = result.indexOf("{", result.indexOf("{") + 1);
        int end = result.lastIndexOf("}");
        try {
            String jsonResponse = result.substring(start, end);
            JSONObject table = new JSONObject(jsonResponse);
            return table;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
