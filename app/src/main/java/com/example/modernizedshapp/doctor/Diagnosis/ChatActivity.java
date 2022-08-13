package com.example.modernizedshapp.doctor.Diagnosis;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.modernizedshapp.R;
import com.example.modernizedshapp.databinding.ActivityChatBinding;
import com.example.modernizedshapp.doctor.Diagnosis.api.MakeCovidRequest;
import com.example.modernizedshapp.doctor.Diagnosis.api.MakeDiagnoseRequest;
import com.example.modernizedshapp.doctor.Diagnosis.api.MakeParseRequest;
import com.example.modernizedshapp.doctor.Diagnosis.api.RequestUtil;
import com.example.modernizedshapp.doctor.Diagnosis.db.Chat;
import com.example.modernizedshapp.doctor.Diagnosis.db.ChatMessage;
import com.example.modernizedshapp.doctor.Diagnosis.db.ChatSQLiteDBHelper;
import com.example.modernizedshapp.doctor.Diagnosis.helpers.GlobalVariables;
import com.example.modernizedshapp.doctor.Diagnosis.helpers.PdfProducer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ChatActivity extends AppCompatActivity implements RequestUtil.ChatRequestListener {

    Animation slide_out_message_box;
    String lastDoctorMessage = "";
    private Boolean isCovid;
    private ActivityChatBinding binding;
    private boolean didAskForEndDiagnose = false;
    private String firstDoctorMessage = "";
    private final View.OnClickListener onEndDiagnoseClick = v -> {
        JSONArray conditions = RequestUtil.getInstance().getConditionsArray();
        GlobalVariables.getInstance().getCurrentChat().get().setConditionsArray(conditions.toString());
        saveOrUpdateChatToDB();
        addUserMessage(getResources().getString(R.string.finish));
        binding.inputLayout.inputsContainer.removeAllViews();
        try {
            onDoctorMessage(conditions.getJSONObject(0).getString("common_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    };
    private Animate animate = new Animate();

    public Boolean getIsCovid() {
        return isCovid;
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.animate = new Animate();
        isCovid = getIntent().getBooleanExtra(getString(R.string.is_covid), false);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        binding.chatLayout.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                ChatActivity.this.scrollChat();
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
            }
        });
        setChatOnCreate();
        if (this.isCovid) {
            new MakeCovidRequest(this);
            binding.inputLayout.inputsContainer.removeAllViews();
        }
        slide_out_message_box = AnimationUtils.loadAnimation(this, R.anim.slide_out_messbox);
    }

    private void scrollChat() {
        binding.scrollViewChat.post(() -> binding.scrollViewChat.fullScroll(View.FOCUS_DOWN));
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    private void setChatOnCreate() {
        Optional<Chat> chat = GlobalVariables.getInstance().getCurrentChat();
        if (chat.isPresent()) {
            try {
                RequestUtil.getInstance().setEvidenceArrayFromString(chat.get().getLastRequest());
                setAllMessages(ChatSQLiteDBHelper.getAllMessagesForChat(this, chat.get().getId()));
                if (chat.get().getConditionsArray() == null) {
                    this.onDoctorQuestionReceived(chat.get().getLastDoctorQuestionId(), new JSONArray(chat.get().getLastDoctorQuestion()), "");
                } else {
                    binding.inputLayout.inputsContainer.removeAllViews();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            this.createFirstMessageFromDoctor();
        }
    }

    private void setAllMessages(List<ChatMessage> messages) {
        if (GlobalVariables.getInstance().getCurrentChat().get().getConditionsArray() == null) {
            for (ChatMessage message : messages) {
                if (message.getIsUserMessage()) {
                    generateNewUserMessageFromStringWithoutSaving(message.getMessage());
                } else {
                    generateNewDoctorMessageFromStringWithoutSaving(message.getMessage());
                }
            }
        } else {
            for (int i = 0; i < messages.size() - 1; i++) {
                if (messages.get(i).getIsUserMessage()) {
                    generateNewUserMessageFromStringWithoutSaving(messages.get(i).getMessage());
                } else {
                    generateNewDoctorMessageFromStringWithoutSaving(messages.get(i).getMessage());
                }
            }
            generateDiagnosisMessageFromStringWithoutSaving(messages.get(messages.size() - 1).getMessage());
        }
    }

    private void createFirstMessageFromDoctor() {
        if (this.isCovid) {
            if (GlobalVariables.getInstance().getCurrentUser().isPresent())
                firstDoctorMessage = getString(R.string.hallo_only) + GlobalVariables.getInstance().getCurrentUser().get().getName()
                        + "! \n" + getString(R.string.select_all_statemenet_that_applay_to_you);
            else {
                firstDoctorMessage = getString(R.string.hello_with_exclamation_mark) + "\n"
                        + getString(R.string.select_all_statemenet_that_applay_to_you);
            }
        } else {
            if (GlobalVariables.getInstance().getCurrentUser().isPresent())
                firstDoctorMessage = getString(R.string.hallo_only) + GlobalVariables.getInstance().getCurrentUser().get().getName()
                        + "! " + getString(R.string.how_can_i_help_you);
            else {
                firstDoctorMessage = getString(R.string.hello_with_exclamation_mark) + getString(R.string.how_can_i_help_you);
            }
        }
        generateNewDoctorMessageFromStringWithoutSaving(firstDoctorMessage);
    }

    private void generateNewUserMessageFromString(String text) {
        if (!this.firstDoctorMessage.equals("")) {
            saveMessageToDB(this.firstDoctorMessage, false);
            this.firstDoctorMessage = "";
        }
        saveMessageToDB(text, true);

        generateNewUserMessageFromStringWithoutSaving(text);
    }

    private void generateNewDoctorMessageFromString(String text) {
        if (!GlobalVariables.getInstance().getCurrentChat().isPresent() || GlobalVariables.getInstance().getCurrentChat().get().getConditionsArray() == null) {
            generateNewDoctorMessageFromStringWithoutSaving(text);
        } else {
            generateDiagnosisMessageFromStringWithoutSaving(text);
        }
        saveMessageToDB(text, false);
    }

    private void generateDiagnosisMessageFromStringWithoutSaving(String text) {
        LinearLayout linearLayout = (LinearLayout) View.inflate(this, R.layout.diagnose_message, null);
        TextView valueTV = linearLayout.findViewById(R.id.standard_info);
        valueTV.setText(text);
        TextView advancedTV = linearLayout.findViewById(R.id.advanced_info);
        StringBuilder stringBuilder = new StringBuilder();
        DecimalFormat df2 = new DecimalFormat("##.##");
        try {
            JSONArray conditions = new JSONArray(GlobalVariables.getInstance().getCurrentChat().get().getConditionsArray());
            for (int i = 0; i < conditions.length(); i++) {
                stringBuilder.append(getString(R.string.name)).append(conditions.getJSONObject(i).getString("common_name")).append("\n");
                stringBuilder.append(getString(R.string.probability)).append(df2.format(conditions.getJSONObject(i).getDouble("probability") * 100));
                stringBuilder.append("%").append("\n\n");
            }
            advancedTV.setText(stringBuilder.toString());
            Button advanced_info_button = linearLayout.findViewById(R.id.advanced_info_button);
            linearLayout.findViewById(R.id.advanced_info_button).setOnClickListener(v -> {
                if (advancedTV.getVisibility() == View.GONE) {
                    advancedTV.setVisibility(View.VISIBLE);
                    advanced_info_button.setText(R.string.show_less);
                    ChatActivity.this.scrollChat();
                } else {
                    advancedTV.setVisibility(View.GONE);
                    advanced_info_button.setText(R.string.show_more);
                    ChatActivity.this.scrollChat();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        binding.chatLayout.addView(linearLayout);
        this.lastDoctorMessage = text;
    }

    public void onExportButtonClick(View v) {
        requestPermission();
    }

    private void requestPermission() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissions(permissions, 300);
    }

    private void makeCovidDiagnoseMessage(String condition, String recommendations) {
        LinearLayout linearLayout = (LinearLayout) View.inflate(this, R.layout.diagnose_message, null);

        TextView mostLikelyConditionsTV = linearLayout.findViewById(R.id.most_likely_conditions);
        mostLikelyConditionsTV.setText(condition);

        TextView valueTV = linearLayout.findViewById(R.id.standard_info);
        valueTV.setText(recommendations);

        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (15 * scale + 0.5f);

        valueTV.setPadding(valueTV.getPaddingLeft(), valueTV.getPaddingTop(), valueTV.getPaddingRight(), dpAsPixels);

        Button advancedInfoButton = linearLayout.findViewById(R.id.advanced_info_button);
        Button exportToPDFFile = linearLayout.findViewById(R.id.export_to_pdf_button);
        advancedInfoButton.setVisibility(View.GONE);
        exportToPDFFile.setVisibility(View.GONE);

        binding.chatLayout.addView(linearLayout);
        this.lastDoctorMessage = condition;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 300) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (GlobalVariables.getInstance().getCurrentChat().isPresent()) {
                    PdfProducer.createPdfFile(this, ChatSQLiteDBHelper.getAllMessagesForChat(this,
                            GlobalVariables.getInstance().getCurrentChat().get().getId()));
                }
            }
        }
    }

    private void generateNewDoctorMessageFromStringWithoutSaving(String text) {
        LinearLayout linearLayout = (LinearLayout) View.inflate(this, R.layout.doctor_message, null);
        TextView valueTV = linearLayout.findViewById(R.id.doctorMessage);
        valueTV.setText(text);
        binding.chatLayout.addView(linearLayout);
        this.lastDoctorMessage = text;
    }

    private void generateNewUserMessageFromStringWithoutSaving(String text) {
        LinearLayout linearLayout = (LinearLayout) View.inflate(this, R.layout.user_message, null);
        TextView valueTV = linearLayout.findViewById(R.id.userMessage);
        valueTV.setText(text);
        binding.chatLayout.addView(linearLayout);
    }

    private void saveMessageToDB(String text, boolean isUserMessage) {
        int chatId = saveOrUpdateChatToDB();
        int id = ChatSQLiteDBHelper.getNextMessageIdAvailable(this, chatId);
        ChatMessage message = new ChatMessage(id, chatId, text, isUserMessage);
        if (!this.isCovid) {
            ChatSQLiteDBHelper.saveMessageDataToDB(this, message);
        }
    }

    private int saveOrUpdateChatToDB() {
        Optional<Chat> chat = GlobalVariables.getInstance().getCurrentChat();
        if (!chat.isPresent()) {
            createNewChatAndSaveToDB();
            chat = GlobalVariables.getInstance().getCurrentChat();
        }
        int chatId = GlobalVariables.getInstance().getCurrentChat().get().getId();
        chat.get().setLastRequest(RequestUtil.getInstance().getStringFromEvidenceArray());
        if (!this.isCovid) {
            ChatSQLiteDBHelper.saveChatDataToDB(this, chat.get());
        }
        return chatId;
    }

    private void createNewChat() {
        Chat currentChat = Chat.builder(ChatSQLiteDBHelper.getNextChatIdAvailable(this), GlobalVariables.getInstance().getCurrentUser().get().getId())
                .conditionArray(null)
                .date(new Date())
                .lastRequest(RequestUtil.getInstance().getStringFromEvidenceArray())
                .build();
        GlobalVariables.getInstance().setCurrentChat(currentChat);
    }

    private void createNewChatAndSaveToDB() {
        this.createNewChat();
        if (!this.isCovid) {
            ChatSQLiteDBHelper.saveChatDataToDB(this, GlobalVariables.getInstance().getCurrentChat().get());
        }
    }

    public void backArrowOnClick(View v) {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    public void sendSymptomsOnClick(View v) {
        if (binding.inputLayout.inputSymptoms.getText().toString().trim().length() > 0) {
            new MakeParseRequest(this, binding.inputLayout.inputSymptoms.getText().toString());
            this.hideMessageBox();
        } else {
            Toast.makeText(this, getString(R.string.input_can_not_be_empty), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDoctorMessage(String msg) {
        generateNewDoctorMessageFromString(msg);
    }

    @Override
    public void addUserMessage(String msg) {
        generateNewUserMessageFromString(msg);
    }

    @Override
    public void hideMessageBox() {
        this.animate.hideMessageBox();
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    private void questionButtonOnClick(String id, String choice, String userMessage, String name) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            jsonObject.put("choice_id", choice);
            jsonObject.put("name", name);
            RequestUtil.getInstance().addToEvidenceArray(jsonObject);
            if (this.isCovid) {
                new MakeCovidRequest(this, userMessage);
            } else {
                new MakeDiagnoseRequest(this, userMessage);
            }
            binding.inputLayout.inputsContainer.removeAllViews();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @Override
    public void onDoctorQuestionReceived(String id, JSONArray msg, String name) {
        GlobalVariables.getInstance().getCurrentChat().get().setLastDoctorQuestionId(id);
        GlobalVariables.getInstance().getCurrentChat().get().setLastDoctorQuestion(msg.toString());
        saveOrUpdateChatToDB();
        binding.inputLayout.inputsContainer.removeAllViews();
        binding.inputLayout.inputsContainer.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_buttons));

        try {
            for (int i = 0; i < msg.length(); i++) {
                Button button = (Button) View.inflate(this, R.layout.answer_button, null);
                button.setText(msg.getJSONObject(i).getString("label"));
                int finalI = i;
                button.setOnClickListener(v -> {
                    try {
                        questionButtonOnClick(id, msg.getJSONObject(finalI).getString("id"),
                                msg.getJSONObject(finalI).getString("label"),
                                name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
                binding.inputLayout.inputsContainer.addView(button);
                Space space = new Space(this);
                space.setLayoutParams(new LinearLayout.LayoutParams(12, 8));
                binding.inputLayout.inputsContainer.addView(space);
            }
            if (!this.isCovid) {
                Button button = (Button) View.inflate(this, R.layout.answer_button, null);
                button.setText(getString(R.string.finish));
                button.setOnClickListener(onEndDiagnoseClick);
                binding.inputLayout.inputsContainer.addView(button);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @Override
    public boolean finishDiagnose() {
        if (didAskForEndDiagnose) {
            return false;
        }
        this.didAskForEndDiagnose = true;
        onDoctorMessage(getString(R.string.finish_diagnose_question));
        Button buttonYes = (Button) View.inflate(this, R.layout.answer_button, null);
        buttonYes.setText(R.string.yes);
        buttonYes.setOnClickListener(onEndDiagnoseClick);

        Button buttonNo = (Button) View.inflate(this, R.layout.answer_button, null);
        buttonNo.setText(R.string.no);
        buttonNo.setOnClickListener(v -> {
            if (this.isCovid) {
                new MakeCovidRequest(this, getString(R.string.no));
            } else {
                new MakeDiagnoseRequest(this, getString(R.string.no));
            }
        });

        binding.inputLayout.inputsContainer.addView(buttonYes);
        Space space = new Space(this);
        space.setLayoutParams(new LinearLayout.LayoutParams(12, 8));
        binding.inputLayout.inputsContainer.addView(space);
        binding.inputLayout.inputsContainer.addView(buttonNo);
        return true;
    }

    @Override
    public void finishCovidDiagnose() {
        JSONArray conditions = RequestUtil.getInstance().getConditionsArray();
        GlobalVariables.getInstance().getCurrentChat().get().setConditionsArray(conditions.toString());
        hideMessageBox();
        try {
            makeCovidDiagnoseMessage(conditions.getJSONObject(0).getString("description"),
                    conditions.getJSONObject(0).getString("label"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @Override
    public void onRequestFailure() {
        this.generateNewDoctorMessageFromString(getString(R.string.error_messsage_response_doctor));
        if (this.isCovid) {
            return;
        }
        if (GlobalVariables.getInstance().getCurrentChat().isPresent()) {
            String id = GlobalVariables.getInstance().getCurrentChat().get().getLastDoctorQuestionId();
            String msg = GlobalVariables.getInstance().getCurrentChat().get().getLastDoctorQuestion();

            if (id == null || msg == null) {
                View view = View.inflate(this, R.layout.msg_input_bar_inner, null);
                binding.inputLayout.inputsContainer.removeAllViews();
                if (this.animate.isRunning()) {
                    slide_out_message_box.setAnimationListener(null);
                }
                binding.inputLayout.inputsContainer.addView(view);

            } else {
                try {
                    this.onDoctorQuestionReceived(GlobalVariables.getInstance().getCurrentChat().get().getLastDoctorQuestionId(),
                            new JSONArray(GlobalVariables.getInstance().getCurrentChat().get().getLastDoctorQuestion()),
                            ""
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        backArrowOnClick(null);
    }

    private class Animate {

        private boolean isRunning = false;

        public boolean isRunning() {
            return isRunning;
        }

        public void hideMessageBox() {
            slide_out_message_box.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    binding.inputLayout.inputsContainer.removeAllViews();
                    Animate.this.isRunning = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            this.isRunning = true;
            binding.inputLayout.inputsContainer.setAnimation(slide_out_message_box);
        }
    }
}