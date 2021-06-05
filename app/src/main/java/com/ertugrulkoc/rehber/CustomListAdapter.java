package com.ertugrulkoc.rehber;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.zip.Inflater;

class CustomListAdapter extends ArrayAdapter<KisiModel> {
    private KisiModel kisi;
    Activity activity;
    int resource;
    LayoutInflater inflater;


    public CustomListAdapter(Context context, int resource, List<KisiModel> kisiModels) {
        super(context, resource,kisiModels);
        }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.satir_stili, null);
        }
       KisiModel kisiModel = getItem(position);
        if (kisiModel != null) {

            TextView kisiAdi = (TextView)v.findViewById(R.id.txtKisiAd);
            TextView kisiNumarasi = (TextView)v.findViewById(R.id.txtKisiNumara);

            if( kisiAdi != null && kisiNumarasi != null){
                kisiNumarasi.setText(kisiModel.getKullaniciTelefon());
                kisiAdi.setText(kisiModel.getKullaniciAdSoyad());
            }
        }
        return v;
    }
}
