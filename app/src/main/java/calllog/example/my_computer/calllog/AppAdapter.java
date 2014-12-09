package calllog.example.my_computer.calllog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by My_Computer on 12/8/2014.
 */

class AppAdapter extends ArrayAdapter<Call_info> {
    private final Context context;
    private final List<Call_info> list;


    public AppAdapter(Context context, int resource, List<Call_info> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(
                    R.layout.item_list_app, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.mode = (ImageView) convertView.findViewById(R.id.mode);
//                viewHolder.number = (TextView) convertView.findViewById(R.id.number);
            viewHolder.duration = (TextView) convertView.findViewById(R.id.duration);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);

        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        Call_info item = list.get(position);
        String category = item.getCategory();
        String type = item.getType();

        if (category.equals("Call")) {

            if (type.equals("INCOMING")) {
                Picasso.with(context).load(android.R.drawable.sym_call_incoming).into(holder.mode);

            } else if (type.equals("OUTGOING")) {
                Picasso.with(context).load(android.R.drawable.sym_call_outgoing).into(holder.mode);

            } else if (type.equals("MISSED")) {
                Picasso.with(context).load(android.R.drawable.sym_call_missed).into(holder.mode);

            } else if (type.equals("Voice_Mail")) {
                Picasso.with(context).load(android.R.drawable.stat_notify_voicemail).into(holder.mode);

            } else if (type.equals("Rejected")) {
                Picasso.with(context).load(android.R.drawable.ic_menu_close_clear_cancel).into(holder.mode);

            } else if (type.equals("Refused_List")) {
//                Picasso.with(context).load(url).into(holder.mode);

            }


        } else if (category.equals("Voice")) {

            if (type.equals("INCOMING")) {
                Picasso.with(context).load(android.R.drawable.ic_menu_save).into(holder.mode);

            } else if (type.equals("OUTGOING")) {
                Picasso.with(context).load(android.R.drawable.ic_btn_speak_now).into(holder.mode);

            } else if (type.equals("MISSED")) {
                Picasso.with(context).load(android.R.drawable.sym_call_missed).into(holder.mode);

            }
        } else if (category.equals("Sms")) {

            if (type.equals("INCOMING")) {
                Picasso.with(context).load(android.R.drawable.ic_menu_save).into(holder.mode);

            } else if (type.equals("OUTGOING")) {
                Picasso.with(context).load(android.R.drawable.ic_btn_speak_now).into(holder.mode);

            } else if (type.equals("MISSED")) {
                Picasso.with(context).load(android.R.drawable.sym_call_missed).into(holder.mode);

            } else if (type.equals("Voice_Mail")) {
                Picasso.with(context).load(android.R.drawable.stat_notify_voicemail).into(holder.mode);

            } else if (type.equals("Rejected")) {
                Picasso.with(context).load(android.R.drawable.ic_menu_close_clear_cancel).into(holder.mode);

            } else if (type.equals("Refused_List")) {
//                Picasso.with(context).load(url).into(holder.mode);

            }


        }
//            holder.number.setText(item.getNumber());

        holder.date.setText(item.getDate());
        int i = Integer.parseInt(item.getDuration());


        holder.duration.setText(String.format("%sh %sm %ss", TimeUnit.SECONDS.toHours(i),
                TimeUnit.SECONDS.toMinutes(i), TimeUnit.SECONDS.toSeconds(i)));


        holder.time.setText(item.getTime());

        return convertView;
    }

    public void updateList(List<Call_info> allContacts1) {

        list.clear();
        list.addAll(allContacts1);
        this.notifyDataSetChanged();
    }

    class ViewHolder {

        ImageView type, mode;
        TextView number, duration, date, time;
    }
}


