package com.ertugrulkoc.rehber;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Rehber_Activity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_PHONE_CALL = 1;
    CustomListAdapter adapter;
    KisiModel kisi;
    List<KisiModel> kisilerListesi;
    String islenecekKisiNo;
    AlertDialog alertKisiKayit, alertKisiDetay, alertKisiGuncelle, alertKisiTiklama;
    int kisiId;
    AlertDialog.Builder alertDialogKisiaydet, alertBuildDetay, alertDialog, alertDialogGuncelle;
    LinearLayout linearLayout, linearLayoutDetay, linearLayoutGuncelle;
    DatabaseHelper databaseHelper;
    private View tasarim;
    private KisiModel kayitEdilecekKisi;
    private Button buttonKisiKaydet, kisiEkleButon, kisiGuncelleButton;
    private String kulEklenmeTarih;
    private ListView listview;
    private TextView alertKisiDetayButton, alertKisiDuzenleButton, alertKisiSilButton, kisiAdet;
    private View alertKisiKaydet, kisiDetay, kisiGuncelle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rehber);
        kisiGuncelle = getLayoutInflater().inflate(R.layout.rehber_duzenle_tasarim, null);
        tasarim = getLayoutInflater().inflate(R.layout.alertdialog_tasarim, null);
        kisiAdet = findViewById(R.id.textViewKisiAdet);
        alertKisiKaydet = getLayoutInflater().inflate(R.layout.rehber_ekleme_tasarim, null);
        kisiDetay = getLayoutInflater().inflate(R.layout.rehber_detay_tasarim, null);
        linearLayout = alertKisiKaydet.findViewById(R.id.linear_rehber_ekleme);
        linearLayoutDetay = kisiDetay.findViewById(R.id.linear_rehber_detay);
        linearLayoutGuncelle = kisiGuncelle.findViewById(R.id.linear_rehber_duzenle);
        kisiGuncelleButton = linearLayoutGuncelle.findViewById(R.id.buttonKisiGuncelle);
        kisiGuncelleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edittxtVeriAlDuzenle(kisiId);
                databaseHelper = new DatabaseHelper(Rehber_Activity.this);
                databaseHelper.veriGuncelle(kayitEdilecekKisi, islenecekKisiNo);
            }
        });
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
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Telefon Arama
                String aranacakNumara = kisilerListesi.get(i).getKullaniciTelefon();
                Intent aramaYap = new Intent(Intent.ACTION_CALL);
                aramaYap.setData(Uri.parse("tel:" + aranacakNumara));
                if (ContextCompat.checkSelfPermission(Rehber_Activity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Rehber_Activity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                } else {
                    startActivity(aramaYap);
                }
            }
        });

        kisilerListesi = new ArrayList<>();
        DatabaseHelper veritabani = new DatabaseHelper(Rehber_Activity.this);
        Cursor cursor = veritabani.veriListele();
        while (cursor.moveToNext()) {//sırasıyla verileri listelememizi sağlıyor.
            kisi = new KisiModel();
            kisi.setKullaniciAdSoyad(cursor.getString(0));
            kisi.setKullaniciTelefon(cursor.getString(1));
            kisi.setKullaniciMail(cursor.getString(2));
            kisi.setKullaniciNot(cursor.getString(3));
            kisi.setKullaniciEklenmeTarih(cursor.getString(4));
            kisi.setKullaniciDogumTarihi(cursor.getString(5));
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

        alertBuildDetay = new AlertDialog.Builder(this);
        alertBuildDetay.setView(kisiDetay);
        alertKisiDetay = alertBuildDetay.create();

        alertDialogGuncelle = new AlertDialog.Builder(this);
        alertDialogGuncelle.setView(kisiGuncelle);
        alertKisiGuncelle = alertDialogGuncelle.create();
    }

    private void listViewGuncelle() {
        adapter = new CustomListAdapter(this, R.layout.satir_stili, kisilerListesi);
        listview.setAdapter(adapter);
        kisiSayisi();
    }

    private void tasarlanmisKisiGuncelle() {
        alertKisiGuncelle.show();
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
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        kulEklenmeTarih = dateFormat.format(calendar.getTime());
        return kulEklenmeTarih;
    }

    private void edittxtTemizleGuncelle(int durum) {
        LinearLayout linearLayout = alertKisiKaydet.findViewById(R.id.linear_rehber_ekleme);
        switch (durum) {
            case 0:
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    Object childView = linearLayout.getChildAt(i);
                    if (childView instanceof EditText) {
                        ((EditText) childView).setText("");
                    }
                }
                break;
            case 1:
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    Object childView = linearLayout.getChildAt(i);
                    if (childView instanceof EditText) {

                    }
                }
                break;

        }
    }

    private void edittxtVeriAl() {
        kayitEdilecekKisi = new KisiModel();
        kayitEdilecekKisi.setKullaniciEklenmeTarih(tarihAl());
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
        boolean varolanKisiKontrol = false;
        for (KisiModel eklenecek : kisilerListesi) {
            if (eklenecek.getKullaniciTelefon().equals(kayitEdilecekKisi.getKullaniciTelefon())) {
                varolanKisiKontrol = true;
                break;
            }
        }
        if (!varolanKisiKontrol) {
            veritabaninaKaydet();
            kisilerListesi.add(kayitEdilecekKisi);
            listViewGuncelle();
            alertKisiKayit.cancel();
            edittxtTemizleGuncelle(0);
            Toast.makeText(this, "Kişi Kayıt Edildi", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "Bu numara zaten kayıtlı" , Toast.LENGTH_SHORT).show();
        }
    }


    private void edittxtVeriAlDuzenle(int kisiSira) {
        kayitEdilecekKisi = new KisiModel();
        kayitEdilecekKisi.setKullaniciEklenmeTarih(tarihAl());
        for (int i = 0; i < linearLayoutGuncelle.getChildCount(); i++) {
            Object childView = linearLayoutGuncelle.getChildAt(i);
            if (childView instanceof EditText) {
                switch (((EditText) childView).getId()) {
                    case R.id.edittxt_duzenle_kisiAdSoyad:
                        kayitEdilecekKisi.setKullaniciAdSoyad(String.valueOf(((EditText) childView).getText()));
                        break;
                    case R.id.edittxt_duzenle_kisiTelefon:
                        kayitEdilecekKisi.setKullaniciTelefon(String.valueOf(((EditText) childView).getText()));
                        break;
                    case R.id.edittxt_duzenle_kisiMail:
                        kayitEdilecekKisi.setKullaniciMail(String.valueOf(((EditText) childView).getText()));
                        break;
                    case R.id.edittxt_duzenle_kisiNot:
                        kayitEdilecekKisi.setKullaniciNot(String.valueOf(((EditText) childView).getText()));
                        break;
                    case R.id.edittxt_duzenle_DogumTarihi:
                        kayitEdilecekKisi.setKullaniciDogumTarihi(String.valueOf(((EditText) childView).getText()));
                        break;
                }
            }
        }
        kisilerListesi.remove(kisiSira);
        kisilerListesi.add(kayitEdilecekKisi);
        listViewGuncelle();
        alertKisiGuncelle.cancel();
        edittxtTemizleGuncelle(0);
        Toast.makeText(this, "Kişi Güncellendi", Toast.LENGTH_SHORT).show();
    }


    private void kisiSayisi() {
        int adet = kisilerListesi.size();
        kisiAdet.setText(getString(R.string.kisiAdet) + adet);

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
        alertKisiTiklama.cancel();
        switch (view.getId()) {
            case R.id.alertDialogDetay:
                alertKisiDetay.show();
                for (int i = 0; i < linearLayoutDetay.getChildCount(); i++) {
                    Object childView = linearLayoutDetay.getChildAt(i);
                    if (childView instanceof EditText) {
                        switch (((EditText) childView).getId()) {
                            case R.id.edittxt_detay_kisiAdSoyad:
                                ((EditText) childView).setText(kisilerListesi.get(kisiId).getKullaniciAdSoyad());
                                break;
                            case R.id.edittxt_detay_kisiTelefon:
                                ((EditText) childView).setText(kisilerListesi.get(kisiId).getKullaniciTelefon());
                                break;
                            case R.id.edittxt_detay_kisiMail:
                                ((EditText) childView).setText(kisilerListesi.get(kisiId).getKullaniciMail());
                                break;
                            case R.id.edittxt_detay_kisiNot:
                                ((EditText) childView).setText(kisilerListesi.get(kisiId).getKullaniciNot());
                                break;
                            case R.id.edittxt_detay_DogumTarihi:
                                ((EditText) childView).setText(kisilerListesi.get(kisiId).getKullaniciDogumTarihi());
                                break;
                        }
                    }
                    if (childView instanceof TextView) {
                        switch (((TextView) childView).getId()) {
                            case R.id.txt_duzenle_kayitTarihi:
                                ((TextView) childView).setText(getString(R.string.kayitTarihi) + " " + kisilerListesi.get(kisiId).getKullaniciEklenmeTarih());
                                break;
                        }
                    }

                }
                listViewGuncelle();
                break;

            case R.id.alertDialogDuzenle:
                kisiDuzenle();
                listViewGuncelle();
                break;

            case R.id.alertDialogSil:
                databaseHelper = new DatabaseHelper(this);
                databaseHelper.veriSil(islenecekKisiNo);
                kisilerListesi.remove(kisiId);
                alertKisiTiklama.cancel();
                listViewGuncelle();
                break;
        }
    }

    private void kisiDuzenle() {
        tasarlanmisKisiGuncelle();
        for (int i = 0; i < linearLayoutGuncelle.getChildCount(); i++) {
            Object childView = linearLayoutGuncelle.getChildAt(i);
            if (childView instanceof EditText) {
                switch (((EditText) childView).getId()) {
                    case R.id.edittxt_duzenle_kisiAdSoyad:
                        ((EditText) childView).setText(kisilerListesi.get(kisiId).getKullaniciAdSoyad());
                        break;
                    case R.id.edittxt_duzenle_kisiTelefon:
                        ((EditText) childView).setText(kisilerListesi.get(kisiId).getKullaniciTelefon());
                        break;
                    case R.id.edittxt_duzenle_kisiMail:
                        ((EditText) childView).setText(kisilerListesi.get(kisiId).getKullaniciMail());
                        break;
                    case R.id.edittxt_duzenle_kisiNot:
                        ((EditText) childView).setText(kisilerListesi.get(kisiId).getKullaniciNot());
                        break;
                    case R.id.edittxt_duzenle_DogumTarihi:
                        ((EditText) childView).setText(kisilerListesi.get(kisiId).getKullaniciDogumTarihi());
                        break;
                }
            }
            if (childView instanceof TextView) {
                switch (((TextView) childView).getId()) {
                    case R.id.txt_duzenle_kayitTarihi:
                        ((TextView) childView).setText(getString(R.string.kayitTarihi) + " " + kisilerListesi.get(kisiId).getKullaniciEklenmeTarih());
                        break;
                }
            }

        }
    }

    public void kisiYakala(String telefonNo, int id) {
        islenecekKisiNo = telefonNo;
        kisiId = id;
    }
}