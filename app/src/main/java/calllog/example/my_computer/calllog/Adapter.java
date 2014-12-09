package calllog.example.my_computer.calllog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


class Adapter extends ArrayAdapter<Call_info> {


    private final List<Call_info> list;
    private DataBaseHelper dataBaseHelper;

    public Adapter(Context context, List<Call_info> objects) {
        super(context, R.layout.custom_row, objects);
        dataBaseHelper = new DataBaseHelper(getContext());
        list = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater;
        View rowView = convertView;
        try {

            if (rowView == null) {
                inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.custom_row, null);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.name = (TextView) rowView.findViewById(R.id.name);
                viewHolder.letterImageView = (LetterImageView) rowView.findViewById(R.id.iv_avatar);
                viewHolder.letterImageView.setOval(true);
                rowView.setTag(viewHolder);

            }

            ViewHolder viewHolder = (ViewHolder) rowView.getTag();
            final Call_info read_contacts = list.get(position);

            if (read_contacts.getName().isEmpty()) {
                viewHolder.name.setText(read_contacts.getNumber());
                viewHolder.letterImageView.setLetter('#');
            } else {
                viewHolder.name.setText(read_contacts.getName());
                viewHolder.letterImageView.setLetter(read_contacts.getName().charAt(0));
            }


            return rowView;
        } catch (Exception e) {
            e.printStackTrace();
            return rowView;
        }
    }


    public void updateList(List<Call_info> allContacts1) {

        list.clear();
        list.addAll(allContacts1);
        this.notifyDataSetChanged();
    }


    class ViewHolder {
        TextView name;
        LetterImageView letterImageView;
    }
}