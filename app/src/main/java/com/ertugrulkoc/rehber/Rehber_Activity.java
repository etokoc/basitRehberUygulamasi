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
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Rehber_Activity extends AppCompatActivity implements View.OnClickListener {

    CustomListAdapter adapter;
    KisiModel kisi;
    List<KisiModel> kisilerListesi;
    String islenecekKisiNo;
    AlertDialog alertKisiKayit;
    AlertDialog alertKisiTiklama;
    int kisiId;
    AlertDialog.Builder alertDialogKisiaydet;
    AlertDialog.Builder alertDialog;
    private View tasarim;
    private KisiModel kayitEdilecekKisi;
    private Button buttonKisiKaydet, kisiEkleButon;
    private String kulEklenmeTarih;
    private ListView listview;
    private TextView alertKisiDetayButton, alertKisiDuzenleButton, alertKisiSilButton,kisiAdet;
    private View alertKisiKaydet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rehber);
        tasarim = getLayoutInflater().inflate(R.layout.alertdialog_tasarim, null);
        kisiAdet = findViewById(R.id.textViewKisiAdet);
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
                        edittxtVeriAl();
                    }
                });
            }
        });

        listview = findViewById(R.id.listViewRehber);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                tasarlanmisAlertDialogGoster();
                kisiYakala(kisilerListesi.get(i).getKullaniciTelefon(), i);
                return false;
            }
        });

        kisilerListesi = new ArrayList<>();
        DatabaseHelper veritabani = new DatabaseHelper(Rehber_Activity.this);
        Cursor cursor = veritabani.veriListele();
        while (cursor.moveToNext()) {//sırasıyla verileri listelememizi sağlıyor.
            kisi = new KisiModel();
            kisi.setKullaniciTelefon(cursor.getString(1));
            kisi.setKullaniciAdSoyad(cursor.getString(0));
            kisilerListesi.add(kisi);
            listViewGuncelle();
        }
        alertDialogOlustur();
    }

    private void alertDialogOlustur() {
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(tasarim);
        alertKisiTiklama = alertDialog.create();
        alertDialogKisiaydet = new AlertDialog.Builder(this);
        alertDialogKisiaydet.setView(alertKisiKaydet);
        alertKisiKayit = alertDialogKisiaydet.create();
    }

    private void listViewGuncelle() {
        adapter = new CustomListAdapter(this, R.layout.satir_stili, kisilerListesi);
        listview.setAdapter(adapter);
        kisiSayisi();
    }

    private void tasarlanmisKisiKayitAlert() {
        alertKisiKayit.show();
    }

    private void tasarlanmisAlertDialogGoster() {
        alertKisiTiklama.show();
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

    private void editxtTemizle(){
        LinearLayout linearLayout = alertKisiKaydet.findViewById(R.id.linear_rehber_ekleme);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            Object childView = linearLayout.getChildAt(i);
            if (childView instanceof EditText) {
               ((EditText) childView).setText("");
            }
        }
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
        kisilerListesi.add(kayitEdilecekKisi);
        listViewGuncelle();
        alertKisiKayit.cancel();
        editxtTemizle();
        Toast.makeText(this, "Kişi Kayıt Edildi", Toast.LENGTH_SHORT).show();
    }

    private void kisiSayisi(){
        int adet = kisilerListesi.size();
        kisiAdet.setText(getString(R.string.kisiAdet)+adet);

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
                listViewGuncelle();
                break;

            case R.id.alertDialogDuzenle:
                listViewGuncelle();
                break;

            case R.id.alertDialogSil:
                DatabaseHelper databaseHelper = new DatabaseHelper(this);
                databaseHelper.veriSil(islenecekKisiNo);
                kisilerListesi.remove(kisiId);
                alertKisiTiklama.cancel();
                listViewGuncelle();
                break;
        }
    }

    public void kisiYakala(String telefonNo, int id) {
        islenecekKisiNo = telefonNo;
        kisiId = id;
    }
}