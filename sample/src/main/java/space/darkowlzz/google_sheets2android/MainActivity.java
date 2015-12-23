package space.darkowlzz.google_sheets2android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;

import space.darkowlzz.gsheets2a.GSheets2A;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView text1 = (TextView) findViewById(R.id.text1);

        GSheets2A gSheets2A = new GSheets2A("1tJ64Y8hje0ui4ap9U33h3KWwpxT_-JuVMSZzxD2Er8k", getApplicationContext());

        gSheets2A.getData(Team.class, new GSheets2A.DataResult() {
            @Override
            public void onReceiveData(ArrayList data) {
                ArrayList<Team> teams = data;
                String allNames = "";

                for (Team t : teams) {
                    allNames += t.name + "\t\t" + t.totalWins + "\n";
                }
                text1.setText(allNames);
            }
        });
    }
}
