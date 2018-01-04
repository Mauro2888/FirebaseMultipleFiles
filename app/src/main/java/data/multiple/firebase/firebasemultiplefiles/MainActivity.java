package data.multiple.firebase.firebasemultiplefiles;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private RecyclerView mRecycler;
  private RecyclerView.LayoutManager mLayoutManager;
  private AdapterReycler mAdapterRecyler;
  private Button mBtnUpload;
  private static final int RESULT_IMAGES = 1023;
  private List<ImageModel>mList;
  private LinearLayout mEmptyView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mEmptyView = findViewById(R.id.emptyView);
    mList = new ArrayList<>();
    mBtnUpload = findViewById(R.id.btn_upload);
    mBtnUpload.setOnClickListener(this);
    mRecycler = findViewById(R.id.recyclerview);
    mLayoutManager = new LinearLayoutManager(this);
    mRecycler.setLayoutManager(mLayoutManager);
    mRecycler.setHasFixedSize(true);
    mAdapterRecyler = new AdapterReycler(MainActivity.this,mList);
    mRecycler.setAdapter(mAdapterRecyler);

  }

  @Override protected void onStart() {
    super.onStart();
    if (mList.size() != 0){
      mEmptyView.setVisibility(View.GONE);
    }
    else {
      mEmptyView.setVisibility(View.VISIBLE);
    }
  }

  @Override protected void onResume() {
    super.onResume();
    if (mList.size() != 0){
      mEmptyView.setVisibility(View.GONE);
    }
    else {
      mEmptyView.setVisibility(View.VISIBLE);
    }

  }

  @Override public void onClick(View v) {
    if (v == mBtnUpload){
      Intent openWindow = new Intent(Intent.ACTION_GET_CONTENT);
      openWindow.setType("image/*");
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
        openWindow.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
      }
      startActivityForResult(Intent.createChooser(openWindow,"Select Images"),RESULT_IMAGES);
    }

  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == RESULT_IMAGES && resultCode == RESULT_OK){
      if (data.getClipData() != null){

        int allData = data.getClipData().getItemCount();

        for (int i = 0; i < allData; i++){

          Uri uri = data.getClipData().getItemAt(i).getUri();

          String nameFile = getRightPathFromProvider(MainActivity.this,uri);
          mList.add(new ImageModel(nameFile));
          Log.d("DATA"," " + nameFile);
          mAdapterRecyler.notifyDataSetChanged();
        }

        Toast.makeText(this, "Images Selected", Toast.LENGTH_SHORT).show();




      }
      else if (data.getData() != null){

        Uri singleImage = data.getData();
        String nameSingle = getRightPathFromProvider(MainActivity.this,singleImage);

        mList.add(new ImageModel(nameSingle));
        mAdapterRecyler.notifyDataSetChanged();
        Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show();
      }
    }
    super.onActivityResult(requestCode, resultCode, data);
  }


  public String getRightPathFromProvider(Context context, Uri uri) {
    Cursor cursor = null;

    String nameImages = null;
    if (uri.getScheme().equals("content")) {
      try {

        cursor = context.getContentResolver().query(uri, null, null, null, null);

        int name = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);

        cursor.moveToFirst();
        nameImages = cursor.getString(name);
      } finally {
        if (cursor != null) {
          cursor.close();
        }
      }
    }
    return nameImages;
  }


}
