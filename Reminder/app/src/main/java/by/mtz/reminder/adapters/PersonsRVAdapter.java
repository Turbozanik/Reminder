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
import by.mtz.reminder.DAO.Person;
import by.mtz.reminder.R;
import by.mtz.reminder.Utils.Utils;
import by.mtz.reminder.activity.ActionDetailsActivity;

/**
 * Created by Roma on 25.10.2016.
 */

public class PersonsRVAdapter extends RecyclerView.Adapter<PersonsRVAdapter.PersonViewHolder>{
    List<Person> personList;
    Context context;
    List<String> str;
    int pos;

    @Override
    public PersonsRVAdapter.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_list_item, parent, false);

        return new PersonsRVAdapter.PersonViewHolder(v);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public PersonsRVAdapter(List<Person> personList, Context context) {
        this.personList = personList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(PersonsRVAdapter.PersonViewHolder holder, int position) {
        Person person = personList.get(position);
        str = new ArrayList<>();

        File mydir = context.getDir(Utils.IMG_DIR_NO_APP, Context.MODE_PRIVATE);
        File lister = mydir.getAbsoluteFile();
        Collections.addAll(str, lister.list());


        for (int j = 0; j < str.size(); j++) {
            Log.e("path1", personList.get(position).getName());
            Log.e("path2", str.get(j));
            Log.e("if", (personList.get(position).getName() + ".jpg").equals(str.get(j)) + "");
            if ((personList.get(position).getName() + ".jpg").equals(str.get(j))) {
                Log.e("inIf", "yes");
                Bitmap someBitmap = BitmapFactory.decodeFile(Utils.IMG_PATH + personList.get(position).getName() + ".jpg");
                holder.roundedImageView.setImageBitmap(someBitmap);
                break;
            } else if (j == str.size() - 1) {
                Log.e("inIf", "no");
                holder.roundedImageView.setImageResource(R.drawable.account_default);
            }
        }
        holder.nameTv.setText(person.getName());
        holder.emailTv.setText(person.getEmail());
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder{

        RecyclerView rv;
        RoundedImageView roundedImageView;
        TextView nameTv;
        TextView emailTv;
        TextView dayTv;
        TextView dateTv;
        CardView cardView;

        public PersonViewHolder(View itemView) {
            super(itemView);
            final Context context = itemView.getContext();

            cardView = (CardView)itemView.findViewById(R.id.personListCardView);
            roundedImageView =(RoundedImageView) itemView.findViewById(R.id.personListItemCircleIv);
            nameTv = (TextView)itemView.findViewById(R.id.personListItemNameTv);
            emailTv = (TextView)itemView.findViewById(R.id.personListItemEmailTv);
            //dayTv = (TextView)itemView.findViewById(R.id.actionListItemDayTv);
            //dateTv = (TextView)itemView.findViewById(R.id.actionListItemDateTv);
        }
    }
}
