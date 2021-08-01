package com.example.covidscenariotracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidscenariotracker.api.ApiUtilities;
import com.example.covidscenariotracker.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static okhttp3.internal.http.HttpDate.format;

public class MainActivity extends AppCompatActivity {
private TextView totalConfirm,totalRecovery,totalActive,totalDeath,totalTests;
private TextView todayConfirm,todayRecovery,todayDeath,lastDate;


private PieChart pieChart;

private List<CountryData> list;
String  country = "Bangladesh";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list= new ArrayList<>();
        if (getIntent().getStringExtra("country")!= null){
            country= getIntent().getStringExtra("country");
        }

        init();

        TextView showCountryName= findViewById(R.id.showCountryNAme);
        showCountryName.setText(country);

        showCountryName.setOnClickListener(v ->
                startActivity(new Intent( MainActivity.this,CountryActivity.class)));

        ApiUtilities.getApiInterFace().getCountryData()
                .enqueue(new Callback<List<CountryData>>() {
                    @Override
                    public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                        list.addAll(response.body());

                //////        /////

                        for (int i=0; i<list.size(); i++){
                            if (list.get(i).getCountry().equals(country)){
                                int confirm= Integer.parseInt(list.get(i).getCases());
                                int active= Integer.parseInt(list.get(i).getActive());
                                int recovered= Integer.parseInt(list.get(i).getRecovered());
                                int death= Integer.parseInt(list.get(i).getDeaths());

                                totalActive.setText(NumberFormat.getInstance().format(active) );
                                totalConfirm.setText(NumberFormat.getInstance().format(confirm) );
                                totalRecovery.setText(NumberFormat.getInstance().format(recovered) );
                                totalDeath.setText(NumberFormat.getInstance().format(death) );

                                todayDeath.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));
                                todayConfirm.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
                                todayRecovery.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));
                                totalTests.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTests())));


                                setText(list.get(i).getUpdated());


                                pieChart.addPieSlice(new PieModel("Confirm",confirm,getResources().getColor(R.color.yellow)));
                                pieChart.addPieSlice(new PieModel("Active",active,getResources().getColor(R.color.blue_pie)));
                                pieChart.addPieSlice(new PieModel("Recovered",recovered,getResources().getColor(R.color.green_pie)));
                                pieChart.addPieSlice(new PieModel("Death",death,getResources().getColor(R.color.red)));

                                pieChart.startAnimation();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CountryData>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void setText(String updated) {
        DateFormat format= new SimpleDateFormat("MMM dd, yyyy");

        long milliSeconds = Long.parseLong(updated);

        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);

        lastDate.setText("Updated at "+format(calendar.getTime()));
    }

    private  void init(){
        todayConfirm=findViewById(R.id.todiConfirm);
        todayRecovery=findViewById(R.id.todayRecovery);
        todayDeath=findViewById(R.id.todayDeath);

        totalActive=findViewById(R.id.totalActive);
        totalConfirm=findViewById(R.id.totalConfirm);
        totalDeath=findViewById(R.id.totalDeath);
        totalRecovery=findViewById(R.id.totalRecovery);
        totalTests=findViewById(R.id.totalTeast);
        pieChart=findViewById(R.id.piechart);
        lastDate=findViewById(R.id.lastDate);
        totalTests=findViewById(R.id.totalTeast);
    }
}