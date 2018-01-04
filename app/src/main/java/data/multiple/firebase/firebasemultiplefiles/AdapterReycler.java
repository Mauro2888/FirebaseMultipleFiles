package data.multiple.firebase.firebasemultiplefiles;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Mauro on 04/01/2018.
 */

public class AdapterReycler extends RecyclerView.Adapter<AdapterReycler.ViewHolder> {

  Context mContext;
  private List<ImageModel>mListModel;

  public AdapterReycler(Context mContext, List<ImageModel> mListModel) {
    this.mContext = mContext;
    this.mListModel = mListModel;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout_main,parent,false);

    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {

    ImageModel mImageData = mListModel.get(position);
    holder.title.setText(mImageData.getmName());

  }

  @Override public int getItemCount() {
    return mListModel.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    public ViewHolder(View itemView) {
      super(itemView);
      title = itemView.findViewById(R.id.filename_txt);
    }
  }
}
