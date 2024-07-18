package vlu.android.numberseven.views;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import vlu.android.numberseven.R;
import vlu.android.numberseven.controllers.UserHelper;

public class UserDetailFragment extends Fragment {

    private static final String ARG_USERNAME = "username";
    private String username;


    private UserHelper db;

    public static UserDetailFragment newInstance(String username) {
        UserDetailFragment fragment = new UserDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(new Bundle());
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
        }
        db = new UserHelper(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_detail, container, false);
        TextView tvUsername = view.findViewById(R.id.tvUsername);
        TextView tvFullName = view.findViewById(R.id.tvFullName);
        TextView tvEmail = view.findViewById(R.id.tvEmail);
        Button btnUpdate = view.findViewById(R.id.btnUpdate);

        String fullname = "";
        String email = "";

        if (username != null) {
            Cursor cursor = db.getUserDetails(username);
            if (cursor.moveToFirst()) {
                int colIndexUsername = cursor.getColumnIndex(UserHelper.COL_1);
                int colIndexFullName = cursor.getColumnIndex(UserHelper.COL_3);
                int colIndexEmail = cursor.getColumnIndex(UserHelper.COL_4);

                if (colIndexUsername >= 0) {
                    tvUsername.setText(cursor.getString(colIndexUsername));
                }
                if (colIndexFullName >= 0) {
                    tvFullName.setText(cursor.getString(colIndexFullName));
                }
                if (colIndexEmail >= 0) {
                    tvEmail.setText(cursor.getString(colIndexEmail));
                }
            }
            cursor.close();
        }
        String finalFullname = fullname;
        String finalEmail = email;
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment updateFragment = UpdateUserFragment.newInstance(username, finalFullname, finalEmail);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_frameLayout, updateFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });



        return view;
    }
}
