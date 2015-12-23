package space.darkowlzz.gsheets2a;

import android.content.Context;

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

public class GSheets2A {
    private static final String GSHEETS_BASE_URL = "https://spreadsheets.google.com/tq?key=";
    private static final String ROWS = "rows";
    private static final String COLUMN = "c";
    private static final String VALUE = "v";
    private String key;
    private Context mCtx;

    /**
     * Data Result interface: To be implemented by the object that should be executed after
     * receiving spreadsheet data.
     */
    public interface DataResult {
        void onReceiveData(ArrayList data);
    }

    /**
     * Data Object interface: To be implemented by Object class that would store the data.
     */
    public interface DataObject {
        void saveData(ArrayList<String> data);
    }

    /**
     * GSheets2A constructor.
     * @param key Google Sheets key. This is the unique key present in the public url of a sheet.
     *            Example: "1tJ64Y8hje0ui4ap9U33h3KWwpxT_-JuZAMSAzxD2Er8"
     * @param ctx Application Context.
     */
    public <T extends DataObject> GSheets2A(String key, Context ctx) {
        this.key = key;
        this.mCtx = ctx;
    }

    /**
     * Returns the gsheet key.
     * @return String gsheet key.
     */
    public String getKey() {
        return key;
    }

    /**
     *
     * @param cls Class of the Data Object
     * @param callback Function to be called after receiving and processing all the data.
     * @param <T> Generic class of the Data Object.
     */
    public <T extends DataObject> void getData(final Class<T> cls, final DataResult callback) {
        final ArrayList<T> objList = new ArrayList();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Cleanup the raw data
                JSONObject jData = processRawData(response);
                try {
                    JSONArray rows = jData.getJSONArray(ROWS);

                    // Iterate through all the rows
                    for (int r = 0; r < rows.length(); ++r) {
                        T aT = cls.newInstance();
                        ArrayList<String> colData = new ArrayList<>();

                        // Iterate through all the columns of current row and add the values to
                        // colData list.
                        JSONArray columns = rows.getJSONObject(r).getJSONArray(COLUMN);
                        for (int c = 0; c < columns.length(); ++c) {
                            if (columns.isNull(c)) {
                                colData.add("");
                            } else {
                                colData.add(columns.getJSONObject(c).getString(VALUE));
                            }
                        }
                        // Save the colData list in the DataObject instance
                        aT.saveData(colData);
                        // Add the DataObject instance to a list
                        objList.add(aT);
                    }

                    // Pass the DataObject list to the callback
                    callback.onReceiveData(objList);

                } catch (JSONException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };

        // Create a Volley Request Queue
        RequestQueue queue = Volley.newRequestQueue(mCtx);

        // Create a StringRequest
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GSHEETS_BASE_URL + key,
                                                        responseListener, errorListener);
        // Add the StringRequest to the request queue
        queue.add(stringRequest);
    }


    /**
     * Process Raw Data: trim and form JSONObject.
     * @param result Raw data fetched from google sheets
     * @return Well formed JSONObject of the data
     */
    protected JSONObject processRawData(String result) {
        int start = result.indexOf("{", result.indexOf("{") + 1);
        int end = result.lastIndexOf("}");
        try {
            String jsonResponse = result.substring(start, end);
            return new JSONObject(jsonResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
