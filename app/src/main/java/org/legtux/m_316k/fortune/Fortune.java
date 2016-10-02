package org.legtux.m_316k.fortune;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Stack;


public class Fortune {

    private int TLDR_LENGTH = 200;

    private ArrayList<String> entries;
    private static Fortune instance;
    private static Context context;
    private HashSet<String> categories;

    private int fortune = 0;

    private Random random = null;

    private Stack<Integer> previous = null;
    private Stack<Integer> next = null;

    public static void setContext(Context context) {
        Fortune.context = context;
    }

    public static Fortune instance(Boolean tldr) {
        if(Fortune.instance == null) {
            Fortune.instance = new Fortune(tldr);
        }
        return Fortune.instance;
    }

    public Fortune(Boolean tldr) {
        this.random = new Random();
        this.entries = new ArrayList<>();
        this.previous = new Stack<>();
        this.next = new Stack<>();

        try {
            AssetManager assets = this.context.getAssets();

            for(String categorie : this.selectedCategories()) {
                InputStream stream = assets.open("fortunes/" + categorie);

                byte[] b = new byte[stream.available()];
                stream.read(b);

                String content = new String(b);

                for (String entry : content.split("\n%\n")) {
                    if(entry.trim().length() > 0) {
                        this.entries.add(entry.trim());
                    }
                }
            }

        } catch (Exception e) {
            // Gotta catch'em all
            Log.e("Meurt", e.getMessage());
        }

        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "fortunes");

        if(dir.exists()) {

            String[] customFiles = dir.list();

            for (String name : customFiles) {
                File f = new File(dir.getAbsolutePath(), name);

                StringBuilder content = new StringBuilder();

                try {
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    String line;

                    while ((line = br.readLine()) != null) {
                        content.append(line);
                        content.append('\n');
                    }

                    br.close();

                    for (String entry : content.toString().split("\n%\n")) {
                        if (entry.trim().length() > 0) {
                            this.entries.add(entry.trim());
                        }
                    }
                } catch (Exception e) {
                    //You'll need to add proper error handling here
                    // No I don't
                    Log.e("Meurt", e.getMessage());
                }
            }
        }

        this.fortune = this.random.nextInt(this.entries.size());
        while(tldr && this.current().length() > TLDR_LENGTH)
            this.fortune = this.random.nextInt(this.entries.size());
    }

    public String previous() {
        this.next.push(this.fortune);
        this.fortune = this.previous.pop();
        return this.current();
    }

    public String current() {
        return this.entries.get(this.fortune);

    }
    public String next(Boolean tldr) {
        this.previous.push(this.fortune);

        if(this.next.empty()) {
            this.fortune = this.random.nextInt(this.entries.size());

            while(tldr && this.current().length() > TLDR_LENGTH)
                this.fortune = this.random.nextInt(this.entries.size());

        } else {
            this.fortune = this.next.pop();
        }

        return this.current();
    }

    public Boolean previousAvailable() {
        return !this.previous.empty();
    }
    public HashSet<String> allCategories() {
        AssetManager assets = this.context.getAssets();

        HashSet<String> categories = new HashSet<String>();

        try {
            for (String file : assets.list("fortunes")) {
                categories.add(file);
            }
        } catch(IOException io) {
            Log.d("io", io.getMessage());
        }

        return categories;
    }

    public HashSet<String> selectedCategories() {
        SharedPreferences preferences = this.context.getSharedPreferences("general", Context.MODE_PRIVATE);
        this.categories = (HashSet<String>) preferences.getStringSet("categories", allCategories());
        return this.categories;
    }
}
