package calllog.example.my_computer.calllog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.listview.Call_info;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by My_Computer on 12/8/2014.
 */

class Detail_Adapter extends ArrayAdapter<Call_info> {
    private final Context context;
    private final List<Call_info> list;

    public Detail_Adapter(Context context, int resource, List<Call_info> list) {
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
            viewHolder.duration = (TextView) convertView.findViewById(R.id.duration);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);

        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        Call_info item = list.get(position);
        String category = item.getCategory();
        String type = item.getType();
        try {


            if (category.equals("Call")) {

                if (type.equals("Incoming")) {
//                    Picasso.with(context).load(android.R.drawable.sym_call_incoming).into(holder.mode);
                    Picasso.with(context).load(R.drawable.incomingcall).into(holder.mode);

                } else if (type.equals("Outgoing")) {
//                    Picasso.with(context).load(android.R.drawable.sym_call_outgoing).into(holder.mode);
                    Picasso.with(context).load(R.drawable.outgoingcall).into(holder.mode);

                } else if (type.equals("Missed")) {
                    Picasso.with(context).load(R.drawable.missedcall).into(holder.mode);

                } else if (type.equals("VoiceMail")) {
                    Picasso.with(context).load(android.R.drawable.stat_notify_voicemail).into(holder.mode);

                } else if (type.equals("Rejected")) {
                    Picasso.with(context).load(R.drawable.rejectedcall).into(holder.mode);

                } else if (type.equals("RefusedList")) {
//                Picasso.with(context).load(url).into(holder.mode);

                }

            } else if (category.equals("Voice")) {

                if (type.equals("Incoming")) {
                    Picasso.with(context).load(android.R.drawable.ic_menu_save).into(holder.mode);

                } else if (type.equals("Outgoing")) {
                    Picasso.with(context).load(android.R.drawable.ic_btn_speak_now).into(holder.mode);

                } else if (type.equals("Missed")) {
                    Picasso.with(context).load(android.R.drawable.sym_call_missed).into(holder.mode);

                }
            } else if (category.equals("Sms")) {

                if (type.equals("Incoming")) {
                    Picasso.with(context).load(R.drawable.incomingmsg).into(holder.mode);

                } else if (type.equals("Outgoing")) {
                    Picasso.with(context).load(R.drawable.outgoingmsg).into(holder.mode);
                }

            }

        } catch (Exception e) {
e.getMessage();

        }
//            holder.number.setText(item.getNumber());

        try {
            holder.date.setText(item.getDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            int[] i = splitToComponentTimes(Integer.valueOf(item.getDuration()));


            if (i[0] >= 1) {

                holder.duration.setText(String.format("%sh %sm %ss", i[0], i[1], i[2]));
            } else if (i[1] >= 1 && i[1] < 59) {
                holder.duration.setText(String.format("%sm %ss", i[1], i[2]));
            } else if (i[2] < 59) {
                holder.duration.setText(String.format("%ss", i[2]));
            }
            holder.time.setText(item.getTime());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public void updateList(List<Call_info> allContacts) {

        list.clear();
        list.addAll(allContacts);
        this.notifyDataSetChanged();
    }

    class ViewHolder {

        ImageView type, mode;
        TextView number, duration, date, time;
    }

    public static int[] splitToComponentTimes(int biggy) {
        int longVal = biggy;
        int hours = longVal / 3600;
        int remainder = longVal - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;
        int[] ints = {hours, mins, secs};
        return ints;
    }
}


