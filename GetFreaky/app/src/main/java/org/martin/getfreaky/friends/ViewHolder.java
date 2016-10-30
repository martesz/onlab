package org.martin.getfreaky.friends;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import org.martin.getfreaky.R;

/**
 * Created by martin on 2016. 10. 25..
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    private CardView cardView;
    private TextView friendId;
    private TextView friendName;
    private TextView friendEmail;
    private CheckBox checkBox;

    public ViewHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.friendCardView);
        friendId = (TextView) itemView.findViewById(R.id.friend_id);
        friendName = (TextView) itemView.findViewById(R.id.friend_name);
        friendEmail = (TextView) itemView.findViewById(R.id.friend_email_address);
        checkBox = (CheckBox) itemView.findViewById(R.id.friend_row_checkbox);
    }

    public CardView getCardView() {
        return cardView;
    }

    public void setFriendIdText(String text) {
        friendId.setText(text);
    }

    public void setFriendNameText(String text) {
        friendName.setText(text);
    }

    public void setFriendEmailText(String text) {
        friendEmail.setText(text);
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }
}
