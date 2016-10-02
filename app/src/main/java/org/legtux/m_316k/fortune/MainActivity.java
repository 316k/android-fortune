package org.legtux.m_316k.fortune;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import static android.R.attr.id;

public class MainActivity extends ActionBarActivity {
    private Fortune fortune;
    private TextView fortuneDisplayer;
    private Button prevFortune;
    private Button nextFortune;
    private ShareActionProvider shareActionProvider;
    private MenuItem tldr;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.preferences = getApplicationContext().getSharedPreferences("general", Context.MODE_PRIVATE);

        Fortune.setContext(getApplicationContext());

        this.fortuneDisplayer = (TextView) findViewById(R.id.fortuneDisplayer);

        this.fortune = Fortune.instance(preferences.getBoolean("tldr", false));
        ((TextView) findViewById(R.id.fortuneDisplayer)).setText(this.fortune.current());

        this.nextFortune = (Button) findViewById(R.id.newFortune);
        nextFortune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fortuneDisplayer.setText(fortune.next(preferences.getBoolean("tldr", false)));
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

        MenuItem share = (MenuItem) menu.findItem(R.id.menu_item_share);
        this.shareActionProvider = (ShareActionProvider) (MenuItemCompat.getActionProvider(share));
        share.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Share intent
                Intent sendIntent = new Intent()
                        .setAction(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_TEXT, fortune.current())
                        .setType("text/plain");
                startActivity(sendIntent);
                return true;
            }
        });

        this.tldr = (MenuItem) menu.findItem(R.id.menu_tldr);
        Boolean tldr_checked = preferences.getBoolean("tldr", false);
        tldr.setChecked(tldr_checked);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_tldr) {
            Boolean tldr_checked = !preferences.getBoolean("tldr", false);

            SharedPreferences.Editor editor = preferences.edit();

            editor.putBoolean("tldr", tldr_checked);

            editor.commit();

            tldr.setChecked(tldr_checked);
        }

        return true;
    }
}
