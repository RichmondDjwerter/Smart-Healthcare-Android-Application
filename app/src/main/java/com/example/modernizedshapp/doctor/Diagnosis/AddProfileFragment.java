package com.example.modernizedshapp.doctor.Diagnosis;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.modernizedshapp.R;
import com.example.modernizedshapp.databinding.FragmentAddProfileBinding;
import com.example.modernizedshapp.doctor.Diagnosis.db.ChatSQLiteDBHelper;
import com.example.modernizedshapp.doctor.Diagnosis.db.User;
import com.example.modernizedshapp.doctor.Diagnosis.helpers.GlobalVariables;
import com.example.modernizedshapp.doctor.Diagnosis.helpers.SharedPreferencesHelper;
import com.example.modernizedshapp.doctor.Diagnosis.helpers.WarningDialog;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProfileFragment extends Fragment {

    private static final long DEFAULT_DATE = System.currentTimeMillis() - 410280000000L;
    private final Calendar myCalendar = Calendar.getInstance();
    GlobalVariables globalVariables = GlobalVariables.getInstance();
    private DatePickerDialog datePickerDialog;
    private final View.OnClickListener dateEditTextFragmentAddProfileOnClick =
            v -> datePickerDialog.show();
    private FragmentAddProfileBinding binding;
    private final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
        setCalendarDate(year, monthOfYear, dayOfMonth);
        updateLabel();
    };

    private boolean isNewUser = false;
    private String userGender;
    private final View.OnClickListener genderFemaleOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            userGender = getString(R.string.female_sign);
            binding.female.clearColorFilter();
            binding.male.setColorFilter(getBlackAndWhiteFilter());
            binding.addUserImage.setImageBitmap(User.getPictureBasedOnGender(userGender));
            binding.beforeAddUserImage.setBackgroundColor(Color.TRANSPARENT);
        }
    };
    private final View.OnClickListener genderMaleOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            userGender = getString(R.string.male_sign);
            binding.male.clearColorFilter();
            binding.female.setColorFilter(getBlackAndWhiteFilter());
            binding.addUserImage.setImageBitmap(User.getPictureBasedOnGender(userGender));
            binding.beforeAddUserImage.setBackgroundColor(Color.TRANSPARENT);
        }
    };

    private WarningDialog warningDialog;


    private AddProfileFragmentListener mListener;
    private final View.OnClickListener addButtonOnClick = v -> {
        if (areInputsEmpty()) {
            Toast.makeText(getContext(), getString(R.string.fill_all_inputs), Toast.LENGTH_SHORT).show();
            return;
        }

        if (isNewUser) {
            AddProfileFragment.this.warningDialog.show();
        } else {
            AddProfileFragment.this.saveOrUpdateUser();
        }


    };

    public AddProfileFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddProfile.
     */
    public static AddProfileFragment newInstance() {
        AddProfileFragment fragment = new AddProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void setCalendarDate(int year, int monthOfYear, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    private boolean areInputsEmpty() {
        String userName = binding.editProfileName.getText().toString();
        Date userBirthDate = myCalendar.getTime();
        return userName == null || userBirthDate == null || userGender == null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (AddProfileFragmentListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private ColorMatrixColorFilter getBlackAndWhiteFilter() {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        return new ColorMatrixColorFilter(matrix);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentAddProfileBinding.inflate(inflater, container, false);
        this.binding.female.setColorFilter(getBlackAndWhiteFilter());
        this.binding.male.setColorFilter(getBlackAndWhiteFilter());

        getArgumentsFromBundle();


        if (!this.isNewUser) {
            setInputsToCurrentUser();
        }

        this.warningDialog = new WarningDialog(this.getContext());

        this.setListeners();
        this.datePickerDialog = new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        this.datePickerDialog.getDatePicker().setMaxDate(DEFAULT_DATE);


        return binding.getRoot();
    }

    private void setListeners() {
        this.binding.male.setOnClickListener(genderMaleOnClick);
        this.binding.female.setOnClickListener(genderFemaleOnClick);
        this.binding.dateEditTextFragmentAddProfile.setOnClickListener(this.dateEditTextFragmentAddProfileOnClick);
        this.binding.fgAddButton.setOnClickListener(addButtonOnClick);
        this.warningDialog.setConfirmationListener(AddProfileFragment.this::saveOrUpdateUser);
    }

    private void saveOrUpdateUser() {
        String userName = binding.editProfileName.getText().toString();
        Date userBirthDate = myCalendar.getTime();

        int currentID;

        if (isNewUser) {
            currentID = ChatSQLiteDBHelper.getNextUserIdAvailable(getContext());
        } else {
            currentID = globalVariables.getCurrentUser().get().getId();
        }

        User user = new User(currentID, userName, userBirthDate, userGender);
        GlobalVariables.getInstance().setCurrentUser(user);
        ChatSQLiteDBHelper.saveUserDataToDB(getContext(), user);
        SharedPreferencesHelper.saveUserId(getContext(), currentID);

        if (mListener != null) {
            mListener.callback(getString(R.string.reload));
        }
        getActivity().onBackPressed();
    }

    private void getArgumentsFromBundle() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.isNewUser = bundle.getBoolean(getString(R.string.is_new_user));
            if (!globalVariables.getCurrentUser().isPresent()) {
                this.isNewUser = true;
            }
        }
    }

    private void setInputsToCurrentUser() {
        if (globalVariables.getCurrentUser().isPresent()) {
            setFieldsFromGlobalVariable();

            binding.editProfileName.setText(globalVariables.getCurrentUser().get().getName());


            this.updateLabel();

            if (userGender.equals(getString(R.string.male_sign))) {
                binding.male.clearColorFilter();
            } else if (userGender.equals(getString(R.string.female_sign))) {
                binding.female.clearColorFilter();
            }


            if (GlobalVariables.getInstance().getCurrentUser().isPresent()) {
                binding.addUserImage.setImageBitmap(globalVariables.getCurrentUser().get().getPicture());
            }

            binding.beforeAddUserImage.setBackgroundColor(Color.TRANSPARENT);
            binding.fgAddButton.setText(getString(R.string.update_string));
        }
    }

    private void setFieldsFromGlobalVariable() {
        if (!globalVariables.getCurrentUser().isPresent()) {
            return;
        }
        this.userGender = globalVariables.getCurrentUser().get().getGender();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(globalVariables.getCurrentUser().get().getBirthDate());
        setCalendarDate(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void updateLabel() {
        binding.dateEditTextFragmentAddProfile.setText(ChatSQLiteDBHelper.DB_DATE_USER_FORMAT.format(myCalendar.getTime()));
    }

    public interface AddProfileFragmentListener {
        void callback(String result);
    }
}