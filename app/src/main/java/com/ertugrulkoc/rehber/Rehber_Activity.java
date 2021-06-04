package com.ertugrulkoc.rehber;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Rehber_Activity extends AppCompatActivity{

    EditText kullaniciAdiSoyadi, telefonNo, dogumTarihi, mail, kisiNot;
    private KisiModel kayitEdilecekKisi;
    private Button buttonKisiKaydet;
    private String kul_AdSoyad, kul_telefonNo, kul_dogumTarihi, kulMail, kulNot, kulEklenmeTarih;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rehber_ekleme_tasarim);
        buttonKisiKaydet = findViewById(R.id.buttonKisiKaydet);
        buttonKisiKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edittxtVeriAl();
            }
        });

    }


    private String tarihAl(){
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        kulEklenmeTarih = dateFormat.format(calendar.getTime());
        return "kulEklenmeTarih";
    }
    private void edittxtVeriAl() {
        kayitEdilecekKisi = new KisiModel();
        kayitEdilecekKisi.setKullaniciEklenmeTarih(tarihAl());

        LinearLayout linearLayout = findViewById(R.id.linear_rehber_ekleme);
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
}