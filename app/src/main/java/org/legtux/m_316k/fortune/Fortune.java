package org.legtux.m_316k.fortune;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;


public class Fortune {
    private ArrayList<String> enteries = null;
    private static Fortune instance = null;
    private static Context context = null;

    private int fortune = 0;

    private Random random = null;

    private Stack<Integer> previous = null;
    private Stack<Integer> next = null;

    public static void setContext(Context context) {
        Fortune.context = context;
    }

    public static Fortune instance() {
        if(Fortune.instance == null) {
            Fortune.instance = new Fortune();
        }
        return Fortune.instance;
    }

    public Fortune() {
        this.random = new Random();
        this.enteries = new ArrayList<>();
        this.previous = new Stack<>();
        this.next = new Stack<>();

        try {
            Resources res = this.context.getResources();
            InputStream stream = res.openRawResource(R.raw.fortunes);

            byte[] b = new byte[stream.available()];
            stream.read(b);

            String content = new String(b);

            for(String entry : content.split("\n%\n")) {
                this.enteries.add(entry.trim());
            }

            Log.d("tarace", "Size : " + Integer.toString(this.enteries.size()));
        } catch (Exception e) {
            // Gotta catch'em all
            Log.e("Meurt", e.getMessage());
        }
        this.next();
    }

    public String previous() {
        this.next.push(this.fortune);
        this.fortune = this.previous.pop();
        return this.current();
    }

    public String current() {
        return this.enteries.get(this.fortune);

    }
    public String next() {
        this.previous.push(this.fortune);

        if(this.next.empty()) {
            this.fortune = this.random.nextInt(this.enteries.size());
        } else {
            this.fortune = this.next.pop();
        }

        return this.current();
    }

    public Boolean previousAvailable() {
        return !this.previous.empty();
    }
}
