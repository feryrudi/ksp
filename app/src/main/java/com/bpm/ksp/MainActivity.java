package com.bpm.ksp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    //url
    String UrlGetData;
    TextView namaNasabah ,kodeNasabah, alamatNasabah,telpNasabah, pekerjaanNasbah, txtInputNomorKTP, kodePinjaman;
    ImageView foto;
    //Tombol
    Button clickButton;
    LinearLayout layoutData;
    //loading
    ProgressDialog progressDialog;
    //private String KEY_NAME = "NAMA";
    String kode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutData = (LinearLayout) findViewById(R.id.tampilData);

        kodeNasabah = (TextView)findViewById(R.id.txtKode);
        namaNasabah = (TextView)findViewById(R.id.txtNama);
        alamatNasabah = (TextView)findViewById(R.id.txtAlamat);
        telpNasabah = (TextView)findViewById(R.id.txtTelp);
        pekerjaanNasbah = (TextView)findViewById(R.id.txtPekerjaan);
        kodePinjaman = (TextView)findViewById(R.id.txtKodePinjaman);
        foto = (ImageView) findViewById(R.id.foto);

        layoutData.setVisibility(View.INVISIBLE);
        UrlGetData = "http://119.2.52.191/android/ksp/getDataNasabah.php";
        clickButton = (Button) findViewById(R.id.btnCekKTP);
        txtInputNomorKTP = (TextView) findViewById(R.id.inputNomorKTP);
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtInputNomorKTP.getText() != "") {

                    getDataNasabah(txtInputNomorKTP.getText().toString());
                }
                else
                    Toast.makeText(getApplicationContext(),"Coba dulu"+ txtInputNomorKTP.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent myIntent = new Intent(MainActivity.this, DetailPinjaman.class);
                Intent myIntent = new Intent(MainActivity.this, DetailPinjaman.class);
                myIntent.putExtra("kodePinjaman", kode);
                startActivity(myIntent);
                //Toast.makeText(getApplicationContext(),"Coba klik", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getDataNasabah(final String KTP) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {

                    tampilDataNasabah(s);

                } catch (JSONException e) {
                    layoutData.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Data Tidak Ditemukan", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    //DATA
                    String data = URLEncoder.encode("KodeKTP", "UTF-8") + "=" +
                            URLEncoder.encode(KTP, "UTF-8");

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
    private void tampilDataNasabah(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Mohon tunggu sebentar..."); // Setting Message
        progressDialog.setTitle("Proses Cek KTP"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        new Thread(new Runnable() {
            public void run() {
                try {
                    //Loading Palsu
                    Thread.sleep(900);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();
        JSONObject obj = jsonArray.getJSONObject(0);
        layoutData.setVisibility(View.VISIBLE);
        kodeNasabah.setText(obj.getString("kode_nasabah"));
        namaNasabah.setText(obj.getString("nama_nasabah"));
        pekerjaanNasbah.setText("PEKERJAAN : " + obj.getString("pekerjaan"));
        telpNasabah.setText("TELP : " + obj.getString("telp_nasabah"));
        alamatNasabah.setText("ALAMAT : " + obj.getString("alamat_nasabah"));
        kodePinjaman.setText(obj.getString("kode_pinjam"));
        Glide.with(getApplicationContext())
                .load("http://119.2.52.191/foto_nasabah/" + obj.getString("kode_nasabah") + ".jpg")
                .crossFade()
                .placeholder(R.mipmap.anonymous)
                .into(foto);
        kode = (obj.getString("kode_pinjam"));
        Toast.makeText(getApplicationContext(), "Data Tersedia!", Toast.LENGTH_SHORT).show();

    }
}
