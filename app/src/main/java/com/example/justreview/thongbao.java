package com.example.justreview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class thongbao extends AppCompatActivity {

    ListView listView;
    String mTitle[] = {"Trọng Công", "Chí Thành"};
    String mDescription[] = {"Thong bao" };
    int images[] = {R.drawable.usericon};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongbao);

        listView = findViewById(R.id.ListView);
        MyAdapter adapter = new MyAdapter( this, mTitle, mDescription, images);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (position ==0 ) {
                    Toast.makeText( thongbao.this , "ThongBao", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String rTitle [];
        String rDescription[];
        int rImgs[];

        MyAdapter (Context c, String title[], String description[], int imgs[]) {
            super(c,R.layout.row, R.id.textView, title);
            this.context = c;
            this.rTitle = title;
            this.rDescription = description;
            this.rImgs = imgs;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.textView);
            TextView mydescription = row.findViewById(R.id.textView);

            images.setImageResource(rImgs[position]);
            myTitle.setText(rTitle[position]);
            mydescription.setText(rDescription[position]);
            return row;
        }
    }
}