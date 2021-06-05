package com.ertugrulkoc.rehber;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Rehber_Activity extends AppCompatActivity implements View.OnClickListener {

    CustomListAdapter adapter;
    KisiModel kisi;
    private View tasarim;
    private KisiModel kayitEdilecekKisi;
    private Button buttonKisiKaydet, kisiEkleButon;
    private String kulEklenmeTarih;
    private ListView listview;
    private TextView alertKisiDetayButton, alertKisiDuzenleButton, alertKisiSilButton;
    private View alertKisiKaydet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rehber);
        tasarim = getLayoutInflater().inflate(R.layout.alertdialog_tasarim, null);

        alertKisiKaydet = getLayoutInflater().inflate(R.layout.rehber_ekleme_tasarim, null);
        buttonKisiKaydet = alertKisiKaydet.findViewById(R.id.buttonKisiKaydet);
        kisiEkleButon = findViewById(R.id.rehbereEkle);
        kisiEkleButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tasarlanmisKisiKayitAlert();

                buttonKisiKaydet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(Rehber_Activity.this, "123123", Toast.LENGTH_SHORT).show();
                        edittxtVeriAl();
                    }
                });
            }
        });

        listview = findViewById(R.id.listViewRehber);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(Rehber_Activity.this, "" + view.getId(), Toast.LENGTH_SHORT).show();
                tasarlanmisAlertDialogGoster();
                return true;
            }
        });

        List<KisiModel> kisiler = new ArrayList<>();
        DatabaseHelper veritabani = new DatabaseHelper(Rehber_Activity.this);
        Cursor cursor = veritabani.veriListele();
        while (cursor.moveToNext()) {//sırasıyla verileri listelememizi sağlıyor.
            kisi = new KisiModel();
            kisi.setKullaniciTelefon(cursor.getString(1));
            kisi.setKullaniciAdSoyad(cursor.getString(0));
            kisiler.add(kisi);
            Toast.makeText(this, "" + kisi.getKullaniciTelefon(), Toast.LENGTH_SHORT).show();
            adapter = new CustomListAdapter(this, R.layout.satir_stili, kisiler);
            listview.setAdapter(adapter);
        }

    }

    private void tasarlanmisKisiKayitAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(alertKisiKaydet);
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private void tasarlanmisAlertDialogGoster() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(tasarim);
        AlertDialog alert = alertDialog.create();
        alert.show();
        alertDialogTuslar();
    }

    //Tuşları tanıtma
    private void alertDialogTuslar() {
        alertKisiDetayButton = tasarim.findViewById(R.id.alertDialogDetay);
        alertKisiDuzenleButton = tasarim.findViewById(R.id.alertDialogDuzenle);
        alertKisiSilButton = tasarim.findViewById(R.id.alertDialogSil);
        alertKisiDetayButton.setOnClickListener(this);
        alertKisiDuzenleButton.setOnClickListener(this);
        alertKisiSilButton.setOnClickListener(this);
    }


    private String tarihAl() {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        kulEklenmeTarih = dateFormat.format(calendar.getTime());
        return kulEklenmeTarih;
    }

    private void edittxtVeriAl() {
        kayitEdilecekKisi = new KisiModel();
        kayitEdilecekKisi.setKullaniciEklenmeTarih(tarihAl());
        LinearLayout linearLayout = alertKisiKaydet.findViewById(R.id.linear_rehber_ekleme);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            Object childView = linearLayout.getChildAt(i);
            if (childView instanceof EditText) {
                switch (((EditText) childView).getId()) {
                    case R.id.edittxt_kisiAdSoyad:
                        kayitEdilecekKisi.setKullaniciAdSoyad(String.valueOf(((EditText) childView).getText()));
                        break;
                    case R.id.edittxt_kisiTelefon:
                        kayitEdilecekKisi.setKullaniciTelefon(String.valueOf(((EditText) childView).getText()));
                        break;
                    case R.id.edittxt_kisiMail:
                        kayitEdilecekKisi.setKullaniciMail(String.valueOf(((EditText) childView).getText()));
                        break;
                    case R.id.edittxt_kisiNot:
                        kayitEdilecekKisi.setKullaniciNot(String.valueOf(((EditText) childView).getText()));
                        break;
                    case R.id.edittxt_DogumTarihi:
                        kayitEdilecekKisi.setKullaniciDogumTarihi(String.valueOf(((EditText) childView).getText()));
                        break;
                }
            }
        }

        veritabaninaKaydet();
    }

    private void veritabaninaKaydet() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.veriEkle(
                kayitEdilecekKisi.getKullaniciAdSoyad(),
                kayitEdilecekKisi.getKullaniciTelefon(),
                kayitEdilecekKisi.getKullaniciMail(),
                kayitEdilecekKisi.getKullaniciNot(),
                kayitEdilecekKisi.getKullaniciEklenmeTarih(),
                kayitEdilecekKisi.getKullaniciDogumTarihi()
        );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alertDialogDetay:
                Toast.makeText(this, "Detay", Toast.LENGTH_SHORT).show();
                break;

            case R.id.alertDialogDuzenle:
                Toast.makeText(this, "Duzenle", Toast.LENGTH_SHORT).show();
                break;

            case R.id.alertDialogSil:
                Toast.makeText(this, "SİL", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}