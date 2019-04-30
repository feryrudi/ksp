package com.bpm.ksp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DetailPinjaman extends AppCompatActivity {
    //Loading
    ProgressDialog progressDialog;
    private String kodePinjaman;
    TextView namaNasabah;
    TextView tanggal, nominal, tujuan,jaminan;

    String UrlGetData, UrlGetAngsuran;
    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
    ListView lsDetailAngsuran;
    List<RowItem> rowItems1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_pinjaman);
        UrlGetData = "http://119.2.52.191/android/ksp/getDetailPinjaman.php";
        UrlGetAngsuran = "http://119.2.52.191/android/ksp/getDetailAngsuran.php";
        tanggal = (TextView)findViewById(R.id.dtTanggal);
        nominal = (TextView)findViewById(R.id.txtNominal);
        tujuan = (TextView)findViewById(R.id.txtTujuan);
        jaminan = (TextView)findViewById(R.id.txtJaminan);
        namaNasabah = (TextView) findViewById(R.id.txtNama);
        lsDetailAngsuran = (ListView) findViewById(R.id.listDetailAngsuran);

        Bundle extras = getIntent().getExtras();
        kodePinjaman = extras.getString("kodePinjaman");
        namaNasabah.setText("PEMINJAM : " + kodePinjaman + "");
        getDataPinjaman(kodePinjaman);
        getDataAngsuran(kodePinjaman);
    }
    private void getDataPinjaman(final String Kode) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    tampilDataPinjaman(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    //DATA
                    String data = URLEncoder.encode("kodePinjaman", "UTF-8") + "=" +
                            URLEncoder.encode(Kode, "UTF-8");

                    URL url = new URL(UrlGetData);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                    wr.write(data);
                    wr.flush();

                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }
    private void tampilDataPinjaman(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        progressDialog = new ProgressDialog(DetailPinjaman.this);
        progressDialog.setMessage("Mohon tunggu sebentar..."); // Setting Message
        progressDialog.setTitle("Proses Cek Pinjaman"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        JSONObject obj = jsonArray.getJSONObject(0);

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();


        tanggal.setText("TANGGAL : " + obj.getString("tanggal") + "");
        jaminan.setText("JAMINAN : " + obj.getString("jaminan") + "");
        tujuan.setText("TUJUAN   : " + obj.getString("tujuan") + "");
        nominal.setText("NOMINAL : " + formatRupiah.format((double)obj.getDouble("nominal")) + "");
    }

    //angsuran
    private void getDataAngsuran(final String KodePinjaman) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {

                    tampilAngsuran(s);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    //DATA
                    String data = URLEncoder.encode("kodePinjaman", "UTF-8") + "=" +
                            URLEncoder.encode(KodePinjaman, "UTF-8");

                    URL url = new URL(UrlGetAngsuran);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                    wr.write(data);
                    wr.flush();

                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }
    private void tampilAngsuran(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);

        rowItems1 = new ArrayList<RowItem>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            RowItem item = new RowItem(obj.getString("tanggal"), "| " + formatRupiah.format((double)obj.getDouble("nominal")) , " | Angsuran Ke -  " + obj.getString("angsuran"));
            rowItems1.add(item);
        }
        ListViewAdapter adapter = new ListViewAdapter(this, R.layout.detail_pinjaman, rowItems1);
        lsDetailAngsuran.setAdapter(adapter);
    }
}