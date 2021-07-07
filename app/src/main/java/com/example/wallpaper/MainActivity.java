package com.example.wallpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wallpaper.Adapter.WallpaperAdapter;
import com.example.wallpaper.Adapter.suggestedAdapter;
import com.example.wallpaper.Interfaces.RecyclerViewIClickListner;
import com.example.wallpaper.Models.SuggestedModel;
import com.example.wallpaper.Models.WallpaperModel;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    static final float END_SCALE = 0.7f;
    ImageView menuIcon;
    LinearLayout contentView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RecyclerView recyclerView, topMostRecyclerView;
    RecyclerView.Adapter adapter;
    WallpaperAdapter wallpaperAdapter;
    private RecyclerViewIClickListner clickListner;
    List<WallpaperModel> wallpaperModelsLists;

    ProgressBar progressBar;
    EditText searchEt;
    ImageView searchIv;

    ArrayList<SuggestedModel> suggestedModels = new ArrayList<>();

    TextView replaceTitle;

    Boolean isScrolling = false;
    int currentItems, totalItems, scrollItems;

    int pageNumber = 1;

    String url = "https://api.pexels.com/v1/curated?page=" + pageNumber + "&per_page=80";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuIcon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content_view);
        progressBar = (ProgressBar) findViewById(R.id.pg_bar);
        clickListner = new RecyclerViewIClickListner() {
            @Override
            public void onItemClick(int position) {
                progressBar.setVisibility(View.VISIBLE);
                find(position);
            }
        };

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationDrawer();
        View headerView = navigationView.getHeaderView(0);
        ImageView appLogo = headerView.findViewById(R.id.app_image);

        recyclerView = findViewById(R.id.recyclerView);
        topMostRecyclerView = findViewById(R.id.suggestd_recyclerView);

        wallpaperModelsLists = new ArrayList<>();

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        wallpaperAdapter = new WallpaperAdapter(MainActivity.this, wallpaperModelsLists);

        recyclerView.setAdapter(wallpaperAdapter);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;

                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = gridLayoutManager.getChildCount();
                totalItems = gridLayoutManager.getItemCount();
                scrollItems = gridLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollItems == totalItems)) {
                    isScrolling = false;
                    fetchWallpaper();

                }
            }
        });


        progressBar.setVisibility(View.VISIBLE);

        replaceTitle = findViewById(R.id.topMostTitle);
        fetchWallpaper();
        suggestedItems();


        searchEt = findViewById(R.id.searchEt);
        searchIv = findViewById(R.id.search_image);
        searchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
                String title=searchEt.getText().toString();
                replaceTitle.setText(title);
                url="https://api.pexels.com/v1/search/?page="+ pageNumber + "&per_page=80&query="+title;
                wallpaperModelsLists.clear();
                fetchWallpaper();
            }
        });
    }

    private void suggestedItems() {
        topMostRecyclerView.setHasFixedSize(true);
        topMostRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        suggestedModels.add(new SuggestedModel(R.drawable.tr, "Trending"));
        suggestedModels.add(new SuggestedModel(R.drawable.nature, "Nature"));
        suggestedModels.add(new SuggestedModel(R.drawable.arc, "Architecture"));
        suggestedModels.add(new SuggestedModel(R.drawable.ppl, "People"));
        suggestedModels.add(new SuggestedModel(R.drawable.bus, "Business"));
        suggestedModels.add(new SuggestedModel(R.drawable.health, "Health"));
        suggestedModels.add(new SuggestedModel(R.drawable.fas, "Fashion"));
        suggestedModels.add(new SuggestedModel(R.drawable.film, "Film"));
        suggestedModels.add(new SuggestedModel(R.drawable.travel, "Travel"));

        adapter = new suggestedAdapter(suggestedModels, clickListner);
        topMostRecyclerView.setAdapter(adapter);


    }


    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        animateNavigationDrawe();

    }

    private void animateNavigationDrawe() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                final float diffscaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffscaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                final float xofffset = drawerView.getWidth() * slideOffset;
                final float xoffsetDiff = contentView.getWidth() * diffscaledOffset / 2;
                final float xTranslation = xofffset - xoffsetDiff;
                contentView.setTranslationX(xTranslation);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                Toast.makeText(this, "home clicked", Toast.LENGTH_SHORT).show();

            case R.id.nav_about:
                Toast.makeText(this, "about clicked", Toast.LENGTH_SHORT).show();

            case R.id.nav_logout:
                Toast.makeText(this, "logout clicked", Toast.LENGTH_SHORT).show();

            case R.id.nav_most_viewd:
                Toast.makeText(this, "most Viewed clicked", Toast.LENGTH_SHORT).show();

            case R.id.nav_trending:
                Toast.makeText(this, "Trending clicked", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private void fetchWallpaper() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.GONE);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("photos");

                            int length = jsonArray.length();

                            for (int i = 0; i < length; i++) {

                                JSONObject object = jsonArray.getJSONObject(i);
                                int id = object.getInt("id");
                                String photographerName = object.getString("photographer");

                                JSONObject objectImage = object.getJSONObject("src");
                                String originalUrl = objectImage.getString("original");
                                String mediumUrl = objectImage.getString("medium");

                                WallpaperModel wallpaperModel = new WallpaperModel(id, originalUrl, mediumUrl, photographerName);
                                wallpaperModelsLists.add(wallpaperModel);
                            }
                            wallpaperAdapter.notifyDataSetChanged();
                            pageNumber++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Authorization", "563492ad6f917000010000015dea67df34ff43fab020ce4c0a685781");
                return params;
            }
        };


        requestQueue.add(request);
    }




    public void find(int position) {


        if(position==0) {
            replaceTitle.setText("Trending");
            url="https://api.pexels.com/v1/search/?page="+ pageNumber + "&per_page=80&query=trending";
            wallpaperModelsLists.clear();
            fetchWallpaper();
            //progressBar.setVisibility(View.GONE);


        }else if(position==1)
        {

            replaceTitle.setText("Nature");
            url="https://api.pexels.com/v1/search/?page="+ pageNumber + "&per_page=80&query=nature";
            wallpaperModelsLists.clear();
            fetchWallpaper();
            //progressBar.setVisibility(View.GONE);
        }
        else if(position==2)
        {
            replaceTitle.setText("Architecture");
            url="https://api.pexels.com/v1/search/?page="+ pageNumber + "&per_page=80&query=architecture";
            wallpaperModelsLists.clear();
            fetchWallpaper();
           // progressBar.setVisibility(View.GONE);
        }

        else if(position==3)
        {
            replaceTitle.setText("People");
            url="https://api.pexels.com/v1/search/?page="+ pageNumber + "&per_page=80&query=people";
            wallpaperModelsLists.clear();
            fetchWallpaper();
            //progressBar.setVisibility(View.GONE);
        }

        else if(position==4)
        {
            replaceTitle.setText("Business");
            url="https://api.pexels.com/v1/search/?page="+ pageNumber + "&per_page=80&query=business";
            wallpaperModelsLists.clear();
            fetchWallpaper();
            //progressBar.setVisibility(View.GONE);
        }

        else if(position==5)
        {
            replaceTitle.setText("Health");
            url="https://api.pexels.com/v1/search/?page="+ pageNumber + "&per_page=80&query=health";
            wallpaperModelsLists.clear();
            fetchWallpaper();
           // progressBar.setVisibility(View.GONE);
        }

        else if(position==6)
        {
            replaceTitle.setText("Fashion");
            url="https://api.pexels.com/v1/search/?page="+ pageNumber + "&per_page=80&query=fashion";
            wallpaperModelsLists.clear();
            fetchWallpaper();
            //progressBar.setVisibility(View.GONE);
        }

        else if(position==7)
        {
            replaceTitle.setText("Film");
            url="https://api.pexels.com/v1/search/?page="+ pageNumber + "&per_page=80&query=film";
            wallpaperModelsLists.clear();
            fetchWallpaper();
            //progressBar.setVisibility(View.GONE);
        }

        else if(position==8)
        {
            replaceTitle.setText("Travel");
            url="https://api.pexels.com/v1/search/?page="+ pageNumber + "&per_page=80&query=travel";
            wallpaperModelsLists.clear();
            fetchWallpaper();
            //
            // progressBar.setVisibility(View.GONE);
        }


    }
}