package by.mtz.reminder.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import by.mtz.reminder.DAO.Action;
import by.mtz.reminder.R;
import by.mtz.reminder.Utils.Utils;
import by.mtz.reminder.activity.ActionDetailsActivity;

/**
 * Created by Roma on 19.10.2016.
 */

public class ActionsRVAdapter extends RecyclerView.Adapter<ActionsRVAdapter.ActionsViewHolder> {

    List<Action> actionList;
    Context context;
    List<String> str;
    int pos;

    @Override
    public ActionsRVAdapter.ActionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.action_list_item, parent, false);

        return new ActionsViewHolder(v);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public ActionsRVAdapter(List<Action> actionList, Context context) {
        this.actionList = actionList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(ActionsRVAdapter.ActionsViewHolder holder, int position) {
        Action action = actionList.get(position);
        str = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE-yyyy/MM/dd-hh:mm:ss", Locale.getDefault());
        String asString = formatter.format(action.getDate());
        String[] parcedDate = asString.split("-");
        Log.e("time", asString);

        String time = parcedDate[2];
        String date = parcedDate[1];
        String day = parcedDate[0];

        File mydir = context.getDir(Utils.IMG_DIR_NO_APP, Context.MODE_PRIVATE);
        File lister = mydir.getAbsoluteFile();
        Collections.addAll(str, lister.list());


        for (int j = 0; j < str.size(); j++) {
                Log.e("path1", actionList.get(position).getName());
                Log.e("path2", str.get(j));
                Log.e("if", (actionList.get(position).getName() + ".jpg").equals(str.get(j)) + "");
                if ((actionList.get(position).getName() + ".jpg").equals(str.get(j))) {
                    Log.e("inIf", "yes");
                    Bitmap someBitmap = BitmapFactory.decodeFile(Utils.IMG_PATH + actionList.get(position).getName() + ".jpg");
                    holder.roundedImageView.setImageBitmap(someBitmap);
                    break;
                } else if (j == str.size() - 1) {
                    Log.e("inIf", "no");
                    holder.roundedImageView.setImageResource(R.drawable.account_default);
                }
        }
        holder.nameTv.setText(action.getName());
        holder.timeTv.setText(time);
        //holder.dateTv.setText(date);
        holder.dayTv.setText(day);
    }

    @Override
    public int getItemCount() {
        return actionList.size();
    }

    public class ActionsViewHolder extends RecyclerView.ViewHolder{

        RecyclerView rv;
        RoundedImageView roundedImageView;
        TextView nameTv;
        TextView timeTv;
        TextView dayTv;
        TextView dateTv;
        CardView cardView;

        public ActionsViewHolder(View itemView) {
            super(itemView);
            final Context context = itemView.getContext();

            cardView = (CardView)itemView.findViewById(R.id.personListCardView);
            roundedImageView =(RoundedImageView) itemView.findViewById(R.id.actionListItemCircleIv);
            nameTv = (TextView)itemView.findViewById(R.id.actionListItemNameTv);
            timeTv = (TextView)itemView.findViewById(R.id.actionListItemTimeTv);
            dayTv = (TextView)itemView.findViewById(R.id.actionListItemDayTv);
            //dateTv = (TextView)itemView.findViewById(R.id.actionListItemDateTv);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(context, ActionDetailsActivity.class);
                    Log.d("pos",getAdapterPosition()+"");
                    myIntent.putExtra(Utils.READ_ACTION_BY_ID,actionList.get(getAdapterPosition()));
                    context.startActivity(myIntent);
                }
            });
        }
    }
}
