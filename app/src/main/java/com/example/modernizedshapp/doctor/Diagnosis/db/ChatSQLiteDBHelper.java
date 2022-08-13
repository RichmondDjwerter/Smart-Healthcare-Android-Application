package com.example.modernizedshapp.doctor.Diagnosis.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatSQLiteDBHelper extends SQLiteOpenHelper {
    public static final DateFormat DB_DATE_USER_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat DB_DATE_MESSAGE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final String DATABASE_NAME = "user_profiles";
    public static final String USER_PROFILE_TABLE_NAME = "users";
    public static final String USER_COLUMN_ID = "user_id";
    public static final String USER_COLUMN_GENDER = "gender";
    public static final String USER_COLUMN_BIRTH_DATE = "birth_date";
    public static final String USER_COLUMN_NAME = "user_name";

    public static final String MESSAGES_TABLE_NAME = "messages";
    public static final String MESSAGES_COLUMN_CHAT_ID = "chat_id";
    public static final String MESSAGES_COLUMN_MESSAGE_ID = "message_id";
    public static final String MESSAGES_COLUMN_MESSAGE = "text";
    public static final String MESSAGES_COLUMN_IS_USER_MESSAGE = "message_is_user_message";

    public static final String CHATS_TABLE_NAME = "chats";
    public static final String CHATS_COLUMN_ID = "chat_id";
    public static final String CHATS_COLUMN_USER_ID = "user_id";
    public static final String CHATS_COLUMN_NEWEST_REQUEST = "request";
    public static final String CHATS_COLUMN_DATETIME = "datetime";
    public static final String CHATS_COLUMN_CONDITIONS_ARRAY = "conditions_array";
    public static final String CHATS_COLUMN_PREVIOUS_DOCTOR_QUESTION = "doc_message";
    public static final String CHATS_COLUMN_PREVIOUS_DOCTOR_QUESTION_ID = "doc_question_id";

    private static final int DATABASE_VERSION = 32;

    /**
     * {@inheritDoc}
     */
    public ChatSQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public static void saveUserDataToDB(Context context, User user) {
        if (isExist(context, user)) {
            updateUserDataToDB(context, user);
            return;
        }
        SQLiteDatabase database = new ChatSQLiteDBHelper(context).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COLUMN_ID, user.getId());
        contentValues.put(USER_COLUMN_NAME, user.getName());
        String date = DB_DATE_USER_FORMAT.format(user.getBirthDate());
        contentValues.put(USER_COLUMN_BIRTH_DATE, date);
        contentValues.put(USER_COLUMN_GENDER, user.getGender());
        database.insert(USER_PROFILE_TABLE_NAME, null, contentValues);
    }

    public static void saveChatDataToDB(Context context, Chat chat) {
        if (isExist(context, chat)) {
            updateChatDataToDB(context, chat);
            return;
        }
        SQLiteDatabase database = new ChatSQLiteDBHelper(context).getWritableDatabase();
        database.execSQL("DELETE FROM " + CHATS_TABLE_NAME + " WHERE " + CHATS_COLUMN_CONDITIONS_ARRAY + " IS NULL");
        ContentValues contentValues = new ContentValues();
        contentValues.put(CHATS_COLUMN_ID, chat.getId());
        contentValues.put(CHATS_COLUMN_USER_ID, chat.getUserId());
        contentValues.put(CHATS_COLUMN_NEWEST_REQUEST, chat.getLastRequest());
        String date = DB_DATE_MESSAGE_FORMAT.format(chat.getDate());
        contentValues.put(CHATS_COLUMN_DATETIME, date);
        contentValues.put(CHATS_COLUMN_CONDITIONS_ARRAY, chat.getConditionsArray());
        contentValues.put(CHATS_COLUMN_PREVIOUS_DOCTOR_QUESTION, chat.getLastDoctorQuestion());
        contentValues.put(CHATS_COLUMN_PREVIOUS_DOCTOR_QUESTION_ID, chat.getLastDoctorQuestionId());
        database.insert(CHATS_TABLE_NAME, null, contentValues);
    }

    public static void saveMessageDataToDB(Context context, ChatMessage message) {
        if (isExist(context, message)) {
            return;
        }
        SQLiteDatabase database = new ChatSQLiteDBHelper(context).getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGES_COLUMN_MESSAGE_ID, message.getId());
        contentValues.put(MESSAGES_COLUMN_CHAT_ID, message.getChatId());
        contentValues.put(MESSAGES_COLUMN_MESSAGE, message.getMessage());
        contentValues.put(MESSAGES_COLUMN_IS_USER_MESSAGE, message.getIsUserMessage());
        database.insert(MESSAGES_TABLE_NAME, null, contentValues);
        database.close();
    }


    private static void updateUserDataToDB(Context context, User user) {
        SQLiteDatabase database = new ChatSQLiteDBHelper(context).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COLUMN_NAME, user.getName());
        String date = DB_DATE_USER_FORMAT.format(user.getBirthDate());
        contentValues.put(USER_COLUMN_BIRTH_DATE, date);
        contentValues.put(USER_COLUMN_GENDER, user.getGender());
        database.update(USER_PROFILE_TABLE_NAME, contentValues, USER_COLUMN_ID + " = ?", new String[]{String.valueOf(user.getId())});
        database.close();
    }

    private static void updateChatDataToDB(Context context, Chat chat) {
        SQLiteDatabase database = new ChatSQLiteDBHelper(context).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CHATS_COLUMN_ID, chat.getId());
        contentValues.put(CHATS_COLUMN_USER_ID, chat.getUserId());
        contentValues.put(CHATS_COLUMN_NEWEST_REQUEST, chat.getLastRequest());
        contentValues.put(CHATS_COLUMN_CONDITIONS_ARRAY, chat.getConditionsArray());
        contentValues.put(CHATS_COLUMN_PREVIOUS_DOCTOR_QUESTION, chat.getLastDoctorQuestion());
        contentValues.put(CHATS_COLUMN_PREVIOUS_DOCTOR_QUESTION_ID, chat.getLastDoctorQuestionId());
        database.update(CHATS_TABLE_NAME, contentValues, CHATS_COLUMN_ID + " = ?", new String[]{String.valueOf(chat.getId())});
        database.close();
    }

    private static boolean isExist(Context context, User user) {
        SQLiteDatabase database = new ChatSQLiteDBHelper(context).getWritableDatabase();
        String[] projection = {
                USER_COLUMN_ID,
        };

        Cursor cursor = database.query(
                ChatSQLiteDBHelper.USER_PROFILE_TABLE_NAME,
                projection,
                USER_COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())},
                null,
                null,
                null
        );

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        database.close();
        return exists;
    }

    private static boolean isExist(Context context, Chat chat) {
        SQLiteDatabase database = new ChatSQLiteDBHelper(context).getWritableDatabase();

        String[] projection = {
                CHATS_COLUMN_ID,
        };

        Cursor cursor = database.query(
                ChatSQLiteDBHelper.CHATS_TABLE_NAME,
                projection,
                CHATS_COLUMN_ID + " = ?",
                new String[]{String.valueOf(chat.getId())},
                null,
                null,
                null
        );


        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        database.close();
        return exists;
    }

    private static boolean isExist(Context context, ChatMessage message) {
        SQLiteDatabase database = new ChatSQLiteDBHelper(context).getWritableDatabase();

        String[] projection = {
                MESSAGES_COLUMN_MESSAGE_ID,
        };

        Cursor cursor = database.query(
                ChatSQLiteDBHelper.MESSAGES_TABLE_NAME,
                projection,
                CHATS_COLUMN_ID + " = ? AND " + MESSAGES_COLUMN_CHAT_ID + " = ?",
                new String[]{String.valueOf(message.getId()), String.valueOf(message.getChatId())},
                null,
                null,
                null
        );

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        database.close();
        return exists;
    }

    public static List<User> getAllUsersFromDB(Context context) {
        SQLiteDatabase database = new ChatSQLiteDBHelper(context).getReadableDatabase();
        String[] projection = {
                USER_COLUMN_ID,
                USER_COLUMN_NAME,
                USER_COLUMN_BIRTH_DATE,
                USER_COLUMN_GENDER
        };

        Cursor cursor = database.query(
                ChatSQLiteDBHelper.USER_PROFILE_TABLE_NAME,      // The table to query
                projection,                                        // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                 // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                      // don't filter by row groups
                null                                      // don't sort
        );

        List<User> usersList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int retId = cursor.getInt(cursor.getColumnIndex(USER_COLUMN_ID));
                String retName = cursor.getString(cursor.getColumnIndex(USER_COLUMN_NAME));
                String retBirthDate = cursor.getString(cursor.getColumnIndex(USER_COLUMN_BIRTH_DATE));
                String retGender = cursor.getString(cursor.getColumnIndex(USER_COLUMN_GENDER));
                try {
                    usersList.add(new User(retId, retName, retBirthDate, retGender));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return usersList;
    }

    public static List<Chat> getAllChatsForUserFromDB(Context context, int userId) {
        SQLiteDatabase database = new ChatSQLiteDBHelper(context).getReadableDatabase();

        String[] projection = {
                "*"
        };

        Cursor cursor = database.query(
                ChatSQLiteDBHelper.CHATS_TABLE_NAME,
                projection,
                CHATS_COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                CHATS_COLUMN_ID + " DESC"
        );

        List<Chat> chatList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                try {
                    int retId = cursor.getInt(cursor.getColumnIndex(CHATS_COLUMN_ID));
                    String newestRequest = cursor.getString(cursor.getColumnIndex(CHATS_COLUMN_NEWEST_REQUEST));
                    String previousDoctorQuestion = cursor.getString(cursor.getColumnIndex(CHATS_COLUMN_PREVIOUS_DOCTOR_QUESTION));
                    String previousDoctorQuestionId = cursor.getString(cursor.getColumnIndex(CHATS_COLUMN_PREVIOUS_DOCTOR_QUESTION_ID));
                    Date date = DB_DATE_MESSAGE_FORMAT.parse(cursor.getString(cursor.getColumnIndex(CHATS_COLUMN_DATETIME)));
                    String conditionsArray = cursor.getString(cursor.getColumnIndex(CHATS_COLUMN_CONDITIONS_ARRAY));
                    chatList.add(Chat.builder(retId, userId)
                            .date(date)
                            .lastRequest(newestRequest)
                            .conditionArray(conditionsArray)
                            .lastDoctorQuestionAndId(previousDoctorQuestion, previousDoctorQuestionId)
                            .build());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
            cursor.close();
            return chatList;
        }
        cursor.close();
        database.close();
        return chatList;
    }

    public static List<ChatMessage> getAllMessagesForChat(Context context, int chatId) {
        SQLiteDatabase database = new ChatSQLiteDBHelper(context).getReadableDatabase();

        String[] projection = {
                "*"
        };

        Cursor cursor = database.query(
                ChatSQLiteDBHelper.MESSAGES_TABLE_NAME,
                projection,
                MESSAGES_COLUMN_CHAT_ID + " = ?",
                new String[]{String.valueOf(chatId)},
                null,
                null,
                MESSAGES_COLUMN_MESSAGE_ID + " ASC"
        );

        List<ChatMessage> messageList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int retId = cursor.getInt(cursor.getColumnIndex(MESSAGES_COLUMN_MESSAGE_ID));
                String text = cursor.getString(cursor.getColumnIndex(MESSAGES_COLUMN_MESSAGE));
                boolean isUserMessage = cursor.getInt(cursor.getColumnIndex(MESSAGES_COLUMN_IS_USER_MESSAGE)) == 1;

                messageList.add(new ChatMessage(retId, chatId, text, isUserMessage));
            } while (cursor.moveToNext());
            database.close();
            cursor.close();
            return messageList;
        }
        database.close();
        cursor.close();
        return null;
    }

    public static User getUserByID(Context context, int id) {
        SQLiteDatabase database = new ChatSQLiteDBHelper(context).getReadableDatabase();

        String[] projection = {
                "*"
        };

        Cursor cursor = database.query(
                ChatSQLiteDBHelper.USER_PROFILE_TABLE_NAME,
                projection,
                USER_COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            int retId = cursor.getInt(cursor.getColumnIndex(USER_COLUMN_ID));
            String retName = cursor.getString(cursor.getColumnIndex(USER_COLUMN_NAME));
            String retBirthDate = cursor.getString(cursor.getColumnIndex(USER_COLUMN_BIRTH_DATE));
            String retGender = cursor.getString(cursor.getColumnIndex(USER_COLUMN_GENDER));
            cursor.close();
            try {
                return new User(retId, retName, retBirthDate, retGender);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        database.close();
        cursor.close();
        return null;
    }

    public static int getNextUserIdAvailable(Context context) {
        SQLiteDatabase database = new ChatSQLiteDBHelper(context).getReadableDatabase();
        String[] projection = {
                USER_COLUMN_ID
        };

        Cursor cursor = database.query(
                ChatSQLiteDBHelper.USER_PROFILE_TABLE_NAME,      // The table to query
                projection,                                        // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                 // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                      // don't filter by row groups
                USER_COLUMN_ID + " DESC"                   // don't sort
        );
        if (cursor.moveToFirst()) {
            int retint = cursor.getInt(cursor.getColumnIndex(USER_COLUMN_ID)) + 1;
            database.close();
            cursor.close();
            return retint;
        }
        database.close();
        cursor.close();
        return 1000;
    }

    public static int getNextChatIdAvailable(Context context) {
        SQLiteDatabase database = new ChatSQLiteDBHelper(context).getReadableDatabase();
        String[] projection = {
                CHATS_COLUMN_ID
        };

        Cursor cursor = database.query(
                ChatSQLiteDBHelper.CHATS_TABLE_NAME,      // The table to query
                projection,                                        // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                 // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                      // don't filter by row groups
                CHATS_COLUMN_ID + " DESC"                   // don't sort
        );
        if (cursor.moveToFirst()) {
            int retint = cursor.getInt(cursor.getColumnIndex(CHATS_COLUMN_ID)) + 1;
            database.close();
            cursor.close();
            return retint;
        }
        database.close();
        cursor.close();
        return 100;
    }

    public static int getNextMessageIdAvailable(Context context, int chatId) {
        SQLiteDatabase database = new ChatSQLiteDBHelper(context).getReadableDatabase();
        String[] projection = {
                MESSAGES_COLUMN_MESSAGE_ID
        };

        Cursor cursor = database.query(
                ChatSQLiteDBHelper.MESSAGES_TABLE_NAME,      // The table to query
                projection,                                        // The columns to return
                MESSAGES_COLUMN_CHAT_ID + " = " + chatId,  // The columns for the WHERE clause
                null,                             // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                      // don't filter by row groups
                MESSAGES_COLUMN_MESSAGE_ID + " DESC"                   // don't sort
        );
        if (cursor.moveToFirst()) {
            int retint = cursor.getInt(cursor.getColumnIndex(MESSAGES_COLUMN_MESSAGE_ID)) + 1;
            database.close();
            cursor.close();
            return retint;
        }
        database.close();
        cursor.close();
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + USER_PROFILE_TABLE_NAME + " (" +
                USER_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                USER_COLUMN_NAME + " TEXT," +
                USER_COLUMN_BIRTH_DATE + " DATE," +
                USER_COLUMN_GENDER + " TEXT check(" + USER_COLUMN_GENDER + " in ('M', 'm', 'F', 'f'))" + ")");

        db.execSQL("CREATE TABLE " + CHATS_TABLE_NAME + " (" +
                CHATS_COLUMN_ID + " INTEGER PRIMARY KEY," +
                CHATS_COLUMN_USER_ID + " INTEGER," +
                CHATS_COLUMN_NEWEST_REQUEST + " VARCHAR(8192)," +
                CHATS_COLUMN_DATETIME + " DATETIME," +
                CHATS_COLUMN_CONDITIONS_ARRAY + " VARCHAR(8192)," +
                CHATS_COLUMN_PREVIOUS_DOCTOR_QUESTION + " VARCHAR(8192)," +
                CHATS_COLUMN_PREVIOUS_DOCTOR_QUESTION_ID + " VARCHAR(32)," +
                " FOREIGN KEY (" + CHATS_COLUMN_USER_ID + ") REFERENCES " + USER_PROFILE_TABLE_NAME + "(" + USER_COLUMN_ID + ")" + ")"
        );

        db.execSQL("CREATE TABLE " + MESSAGES_TABLE_NAME + " (" +
                MESSAGES_COLUMN_MESSAGE_ID + " INTEGER," +
                MESSAGES_COLUMN_CHAT_ID + " INTEGER," +
                MESSAGES_COLUMN_MESSAGE + " VARCHAR(1024)," +
                MESSAGES_COLUMN_IS_USER_MESSAGE + " INTEGER," +
                " FOREIGN KEY (" + MESSAGES_COLUMN_CHAT_ID + ") REFERENCES " + CHATS_TABLE_NAME + "(" + CHATS_COLUMN_ID + ")," +
                " PRIMARY KEY " + "(" + MESSAGES_COLUMN_MESSAGE_ID + ", " + MESSAGES_COLUMN_CHAT_ID + ")" + ")"
        );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MESSAGES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CHATS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USER_PROFILE_TABLE_NAME);
        onCreate(db);
    }

}
