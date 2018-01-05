package data.multiple.firebase.firebasemultiplefiles;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Mauro on 04/01/2018.
 */

public class AdapterReycler extends RecyclerView.Adapter<AdapterReycler.ViewHolder> {

  Context mContext;
  private List<ImageModel>mListModel;
  private List<String>mNameList;

  public AdapterReycler(Context mContext, List<ImageModel> mListModel, List<String> mNameList) {
    this.mContext = mContext;
    this.mListModel = mListModel;
    this.mNameList = mNameList;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout_main,parent,false);

    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {

    ImageModel mImageData = mListModel.get(position);
    holder.title.setText(mImageData.getmName());

    String donePos = mNameList.get(position);
    if (donePos.equals("uploading")){
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        holder.done.setImageDrawable(mContext.getDrawable(R.drawable.ic_star_upload));
      }
    }
    else if(donePos.equals("completed")){
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        holder.done.setImageDrawable(mContext.getDrawable(R.drawable.ic_completed_upload));
      }
    }

  }

  @Override public int getItemCount() {
    return mListModel.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    ImageView done;
    public ViewHolder(View itemView) {
      super(itemView);
      title = itemView.findViewById(R.id.filename_txt);
      done = itemView.findViewById(R.id.image_done);
    }
  }
}
