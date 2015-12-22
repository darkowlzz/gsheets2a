package space.darkowlzz.google_sheets2android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import space.darkowlzz.gsheets2a.GSheets2A;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView text1 = (TextView) findViewById(R.id.text1);

        GSheets2A gSheets2A = new GSheets2A("1rE6mYXea5ZG_MOgQR7Jh1419-fUiH3sz4x35nvdfH9Q", getApplicationContext());
        gSheets2A.getData(new GSheets2A.DataResult() {
            @Override
            public void onReceiveData(JSONObject jsonObject) {
                try {
                    JSONArray rows = jsonObject.getJSONArray("rows");
                    JSONObject row = rows.getJSONObject(0);
                    JSONArray columns = row.getJSONArray("c");
                    String name = columns.getJSONObject(3).getString("v");
                    text1.setText(name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
