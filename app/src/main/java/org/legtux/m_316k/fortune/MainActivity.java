package org.legtux.m_316k.fortune;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
    private Fortune fortune;
    private TextView fortuneDisplayer;
    private Button prevFortune;
    private Button nextFortune;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fortune.setContext(getApplicationContext());

        this.fortuneDisplayer = (TextView) findViewById(R.id.fortuneDisplayer);

        this.fortune = Fortune.instance();
        ((TextView) findViewById(R.id.fortuneDisplayer)).setText(this.fortune.current());

        this.nextFortune = (Button) findViewById(R.id.newFortune);
        nextFortune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fortuneDisplayer.setText(fortune.next());
                prevFortune.setEnabled(true);
            }
        });

        this.prevFortune = (Button) findViewById(R.id.prev);
        prevFortune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fortuneDisplayer.setText(fortune.previous());
                prevFortune.setEnabled(fortune.previousAvailable());
            }
        });
        prevFortune.setEnabled(fortune.previousAvailable());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem about = (MenuItem) menu.findItem(R.id.action_about);
        about.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getApplicationContext(), getString(R.string.about_text), Toast.LENGTH_LONG).show();
                return true;
            }
        });

        MenuItem settings = (MenuItem) menu.findItem(R.id.action_settings);
        settings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getApplicationContext(), "No.", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        MenuItem share = (MenuItem) menu.findItem(R.id.action_share);
        share.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getApplicationContext(), "No.", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
