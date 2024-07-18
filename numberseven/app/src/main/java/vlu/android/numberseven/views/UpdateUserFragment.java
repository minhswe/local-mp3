package vlu.android.numberseven.views;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

import vlu.android.numberseven.R;
import vlu.android.numberseven.controllers.UserHelper;

public class UpdateUserFragment extends Fragment {
    private static final String ARG_USERNAME = "username";
    private static final String ARG_FULLNAME = "fullname";
    private static final String ARG_EMAIL = "email";
    private String username;
    private UserHelper db;
    private EditText etUsername;
    private EditText etFullName;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnSave1;
    private Button btnBack;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public UpdateUserFragment() {
    }

    public static UpdateUserFragment newInstance(String username, String fullname, String email) {
        UpdateUserFragment fragment = new UpdateUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_FULLNAME, fullname);
        args.putString(ARG_EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
        }
        db = new UserHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_user, container, false);

        etUsername = view.findViewById(R.id.etUsername);
        etFullName = view.findViewById(R.id.etFullName);
        etEmail = view.findViewById(R.id.etEmail);
        btnSave1 = view.findViewById(R.id.btnSave);
        btnBack = view.findViewById(R.id.btnBack);
        etPassword = view.findViewById(R.id.etPassword);

        etUsername.setEnabled(false);

        if (username != null) {
            Cursor cursor = db.getUserDetails(username);
            if (cursor.moveToFirst()) {
                int colIndexUsername = cursor.getColumnIndex(UserHelper.COL_1);
                int colIndexFullName = cursor.getColumnIndex(UserHelper.COL_3);
                int colIndexEmail = cursor.getColumnIndex(UserHelper.COL_4);

                if (colIndexUsername >= 0) {
                    etUsername.setText(cursor.getString(colIndexUsername));
                }
                if (colIndexFullName >= 0) {
                    etFullName.setText(cursor.getString(colIndexFullName));
                }
                if (colIndexEmail >= 0) {
                    etEmail.setText(cursor.getString(colIndexEmail));
                }
            }
            cursor.close();
        }

        btnSave1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newFullName = etFullName.getText().toString();
                String newEmail = etEmail.getText().toString();
                String newPassword = etPassword.getText().toString();

                if (!TextUtils.isEmpty(newPassword) && !isPasswordComplex(newPassword)) {
                    Toast.makeText(getContext(), "Mật khẩu phải chứa ít nhất một chữ hoa, một chữ thường, một số và một ký tự đặc biệt.", Toast.LENGTH_LONG).show();
                    return;
                }

                showConfirmationDialog(newFullName, newEmail, newPassword);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment userDetailFragment = UserDetailFragment.newInstance(username);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_frameLayout, userDetailFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    private boolean isPasswordComplex(String password) {
        // Kiểm tra độ phức tạp của mật khẩu
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return Pattern.matches(passwordPattern, password);
    }

    private void showConfirmationDialog(final String newFullName, final String newEmail, final String newPassword) {
        new AlertDialog.Builder(getContext())
                .setTitle("Xác nhận cập nhật")
                .setMessage("Bạn có chắc chắn muốn cập nhật thông tin?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    boolean updatedDetails = db.updateUserDetails(username, newFullName, newEmail);
                    boolean updatedPassword = newPassword.isEmpty() || db.updateUserPassword(username, newPassword);

                    if (updatedDetails && updatedPassword) {
                        Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
}