package siemens.PhotoGallery.helpfulStructures;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AccountListRowAdapter  extends BaseAdapter {
    private Activity activity;
    private List<UserAccount> userAccounts;
    private List<String> userAccountEmails = new ArrayList<>();


    public AccountListRowAdapter(Activity activity,List<UserAccount> userAccounts) {
        this.activity = activity;
        this.userAccounts = userAccounts;
        for (UserAccount userAccount : userAccounts) userAccountEmails.add(userAccount.getUserEmail());
    }
// override other abstract methods here

    @Override
    public int getCount() {
        return userAccounts.size();
    }

    @Override
    public Object getItem(int position) {
        return userAccounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) { // adaptet to show the accounts in a list format
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(android.R.layout.simple_list_item_2, container ,false);
        }

        ((TextView) convertView.findViewById(android.R.id.text1))
                .setText(userAccountEmails.get(position));
        if (userAccounts.get(position).getNumberOfImages() > 0 ){
            String message = Integer.toString(userAccounts.get(position).getNumberOfImages()) + " images";
            ((TextView) convertView.findViewById(android.R.id.text2))
                    .setText(message);
        }

        return convertView;
    }
}
