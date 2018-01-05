package data.multiple.firebase.firebasemultiplefiles;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

  private RecyclerView mRecycler;
  private RecyclerView.LayoutManager mLayoutManager;
  private AdapterReycler mAdapterRecyler;
  private static final int RESULT_IMAGES = 1023;
  private List<ImageModel>mList;
  private List<String>mListStatus;
  private LinearLayout mEmptyView;
  private StorageReference mFireBaseRef;
  private ProgressBar mProgressBar;


  private BottomNavigationView mBottomNav ;
  private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()){
        case R.id.upload_menu:
          Intent openWindow = new Intent(Intent.ACTION_GET_CONTENT);
          openWindow.setType("image/*");
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            openWindow.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
          }
          startActivityForResult(Intent.createChooser(openWindow,"Select Images"),RESULT_IMAGES);
          Toast.makeText(MainActivity.this, "Upload", Toast.LENGTH_SHORT).show();
          return true;

        case R.id.login:
          Toast.makeText(MainActivity.this, "Login", Toast.LENGTH_SHORT).show();
          return true;
      }
      return false;
    }
  };
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mBottomNav = findViewById(R.id.bottom_nav);
    mProgressBar = findViewById(R.id.progress_done);
    mBottomNav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    mEmptyView = findViewById(R.id.emptyView);
    mList = new ArrayList<>();
    mListStatus = new ArrayList<>();
    mRecycler = findViewById(R.id.recyclerview);
    mLayoutManager = new LinearLayoutManager(this);
    mRecycler.setLayoutManager(mLayoutManager);
    mRecycler.setHasFixedSize(true);
    mAdapterRecyler = new AdapterReycler(MainActivity.this,mList,mListStatus);
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


  @Override protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
    if (requestCode == RESULT_IMAGES && resultCode == RESULT_OK){
      if (data.getClipData() != null){

        int allData = data.getClipData().getItemCount();

        for (int i = 0; i < allData; i++){
          Uri uri = data.getClipData().getItemAt(i).getUri();
          String nameFile = getRightPathFromProvider(MainActivity.this,uri);
          mList.add(new ImageModel(nameFile));
          mListStatus.add("uploading");

          //FireBase Storage
          mFireBaseRef = FirebaseStorage.getInstance().getReference().child("/images").child(nameFile);
          final int finalI = i;
          mFireBaseRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
              mAdapterRecyler.notifyDataSetChanged();
              mListStatus.remove(finalI);
              mListStatus.add(finalI,"completed");
              Toast.makeText(MainActivity.this, "Completed", Toast.LENGTH_SHORT).show();
            }
          })
              .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                  double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                  mProgressBar.setProgress((int) progress);

                }
              });

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
      try {

        String [] proj = { MediaStore.Images.Media.DISPLAY_NAME};

        cursor = context.getContentResolver().query(uri, proj, null, null, null);

        int name = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);

        cursor.moveToFirst();
        nameImages = cursor.getString(name);
      } finally {
        if (cursor != null) {
          cursor.close();
        }
      }
    return nameImages;
  }


}
