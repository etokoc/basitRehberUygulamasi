package com.ertugrulkoc.rehber;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLO_AD = "Kisiler";
    private static final String ROW_ID_ = "id";
    private static final String AdSoyad = "adsoyad";
    private static final String TelefonNo = "tel";
    private static final String Mail = "mail";
    private static final String KisiselNot = "kisiselNot";
    private static final String EklenmeTarih = "eklemeTarih";
    private static final String DogumTarihi = "dogumTarihi";
    static String databaseName = "vtRehber";
    static int version = 2;


    public DatabaseHelper(Context context) {
        super(context, databaseName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLO_AD + "("
                + ROW_ID_ + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AdSoyad + " TEXT NOT NULL, "
                + TelefonNo + " TEXT NOT NULL,"
                + Mail + "TEXT,"
                + KisiselNot + "TEXT,"
                + EklenmeTarih + "TEXT NOT NULL,"
                +DogumTarihi+"TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLO_AD);
        onCreate(db);
    }

    public void veriEkle(String kulAdSoyad, String kulTelefon, String kulMail,String kulNot, String kulEklenmeTarih,String kulDogum) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AdSoyad, kulAdSoyad);
        values.put(TelefonNo, kulTelefon);
        values.put(Mail, kulMail);
        values.put(KisiselNot, kulNot);
        values.put(EklenmeTarih, kulEklenmeTarih);
        values.put(DogumTarihi, kulDogum);

        db.insert(TABLO_AD,null,values);
        db.close();
    }
}
