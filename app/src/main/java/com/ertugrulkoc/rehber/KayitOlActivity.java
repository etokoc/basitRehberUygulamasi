package com.ertugrulkoc.rehber;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class KayitOlActivity extends AppCompatActivity implements View.OnClickListener {
    final static String XML_DOSYA_ADI = "kullaniciVeri";
    final static String SUTUN_KULLANICI_AD = "kullanici";
    private static final int REQUEST_STORAGE = 1;
    String kullaniciAdi = "";
    String kullaniciSifre = "";
    Button kisiKaydetButton;
    EditText kullaniciAdEdit, kullaniciSifreEdit;
    Element password, name;
    private String kullaniciAdCekilenVeri;
    private String kullaniciSifreCekilenVeri;

    public String getKullaniciAdCekilenVeri() {
        return kullaniciAdCekilenVeri;
    }

    public void setKullaniciAdCekilenVeri(String kullaniciAdCekilenVeri) {
        this.kullaniciAdCekilenVeri = kullaniciAdCekilenVeri;
    }

    public String getKullaniciSifreCekilenVeri() {
        return kullaniciSifreCekilenVeri;
    }

    public void setKullaniciSifreCekilenVeri(String kullaniciSifreCekilenVeri) {
        this.kullaniciSifreCekilenVeri = kullaniciSifreCekilenVeri;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);
        kisiKaydetButton = findViewById(R.id.kisiKaydetButton);
        kullaniciAdEdit = findViewById(R.id.kullaniciAdKayit);
        kullaniciSifreEdit = findViewById(R.id.parolaKayit);
        kisiKaydetButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        kullaniciAdi = kullaniciAdEdit.getText().toString();
        kullaniciSifre = kullaniciSifreEdit.getText().toString();
        if (kullaniciAdi != null && !kullaniciAdi.equals("") && kullaniciSifre != null && !kullaniciSifre.equals("")) {
            xmlVeriEkle(kullaniciAdi, kullaniciSifre);

        } else {
            Toast.makeText(this, "Bilgileri Eksiksiz Giriniz !", Toast.LENGTH_SHORT).show();
        }
    }


    private void xmlVeriEkle(String kullaniciAdi, String kullaniciSifre) {
        try {
            // Document olustur
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document dom = builder.newDocument();

            // root ekle
            Element root = dom.createElement(SUTUN_KULLANICI_AD);
            dom.appendChild(root);

            // taglere ekle (name, password)
            name = dom.createElement("kullaniciAd");
            name.setTextContent(kullaniciAdi);
            password = dom.createElement("sifre");
            password.setTextContent(kullaniciSifre);
            root.appendChild(name);
            root.appendChild(password);

            // Xmle yaz

            if (ContextCompat.checkSelfPermission(KayitOlActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(KayitOlActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
            } else {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.transform(new DOMSource(dom), new StreamResult(new File(android.os.Environment.getExternalStorageDirectory(), XML_DOSYA_ADI + ".xml")));
                tr.transform(new DOMSource(dom), new StreamResult(System.out));
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("kulAd", kullaniciAdi);
                startActivity(intent);
                this.finish();
            }
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void xmlVeriCek() {
        try {
            // Xmli doma cevir
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document dom = builder.parse(new File(Environment.getExternalStorageDirectory(), XML_DOSYA_ADI + ".xml"));

            // xml yapısı
            dom.normalizeDocument();

            // rootu al
            Element root = dom.getDocumentElement();

            // veriyi yazdir
            setKullaniciAdCekilenVeri(root.getElementsByTagName("kullaniciAd").item(0).getTextContent());
            setKullaniciSifreCekilenVeri(root.getElementsByTagName("sifre").item(0).getTextContent());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}