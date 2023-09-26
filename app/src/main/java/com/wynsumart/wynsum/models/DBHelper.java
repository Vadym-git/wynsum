package com.wynsumart.wynsum.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.wynsumart.wynsum.interfaces.Subscriber;

public class DBHelper {
    private final SQLiteDatabase dataBase;
    private static DBHelper instance;
    private final List<Subscriber> subscribers;

    public DBHelper(SQLiteDatabase dataBase) {
        this.dataBase = dataBase;
        subscribers = new ArrayList<>();
    }

    public static synchronized DBHelper getInstance(SQLiteDatabase dataBase) {
        if (instance == null) {
            instance = new DBHelper(dataBase);
        }
        return instance;
    }

//    public List<Integer> getTargetsId() {
//        List<Integer> ids = new ArrayList<Integer>();
//        String[] columns = {"id"};
//        Cursor cursor = dataBase.query(DataBase.TARGETS_TABLE, columns, null, null, null, null, null);
//        // Iterate over the rows in the Cursor and extract the data
//        while (cursor.moveToNext()) {
//            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
//            ids.add(id);
//        }
//        cursor.close();
//        return ids;
//    }

    public List<MeditationTargetContainer> getAllTargets() {
        List<MeditationTargetContainer> targets = new ArrayList<MeditationTargetContainer>();
        Cursor cursor = dataBase.query(DataBase.TARGETS_TABLE, null, null, null, null, null, null);
        // Iterate over the rows in the Cursor and extract the data
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            String short_description = cursor.getString(cursor.getColumnIndexOrThrow("short_description"));
            String icon = cursor.getString(cursor.getColumnIndexOrThrow("icon"));
            String guide = cursor.getString(cursor.getColumnIndexOrThrow("guide"));
            MeditationTargetContainer target = new MeditationTargetContainer(id, name, icon, description,
                    short_description, guide);
            targets.add(target);
        }
        cursor.close();
        return targets;
    }

    public void insertTargetToDb(int id, String name, String icon, String description, String
            short_description, String guide) {
        // Create a ContentValues object with the values to insert
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        values.put("icon", icon);
        values.put("description", description);
        values.put("short_description", short_description);
        values.put("guide", guide);
        dataBase.insert(DataBase.TARGETS_TABLE, null, values);
    }

    public void clearTargets(){
        dataBase.delete(DataBase.TARGETS_TABLE, null, null);
    }


//    public MeditationTargetContainer selectTargetByPosition(long position) {
//        // Define the columns to retrieve
//        String[] projection = {"*"};
//        // Define the sort order
////        String sortOrder = "id ASC";
//        // Define the position of the row to select
//        // Define the number of rows to retrieve (in this case, only 1)
//        int limit = 1;
//        // Query the database and get a Cursor object
//        Cursor cursor = dataBase.query(DataBase.TARGETS_TABLE, projection, null, null, null, null, null, position + "," + limit);
//        // Extract the data from the Cursor
//        if (cursor.moveToFirst()) {
//            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
//            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
//            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
//            String short_description = cursor.getString(cursor.getColumnIndexOrThrow("short_description"));
//            String icon = cursor.getString(cursor.getColumnIndexOrThrow("icon"));
//            String guide = cursor.getString(cursor.getColumnIndexOrThrow("guide"));
//            cursor.close();
//            return new MeditationTargetContainer(id, name, icon, description,
//                    short_description, guide);
//        }
//        return null;
//    }

//    public MeditationTargetContainer getTargetsById(int targetId) {
//        MeditationTargetContainer target;
//        String[] columns = {"*"};
//        String selection = "id=?";
//        String[] selectionArgs = {String.valueOf(targetId)};
//        Cursor cursor = dataBase.query(DataBase.TARGETS_TABLE, columns, selection, selectionArgs, null, null, null);
//        // Iterate over the rows in the Cursor and extract the data
//        while (cursor.moveToNext()) {
//            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
//            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
//            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
//            String short_description = cursor.getString(cursor.getColumnIndexOrThrow("short_description"));
//            String icon = cursor.getString(cursor.getColumnIndexOrThrow("icon"));
//            String guide = cursor.getString(cursor.getColumnIndexOrThrow("guide"));
//            cursor.close();
//            return new MeditationTargetContainer(id, name, icon, description,
//                    short_description, guide);
//        }
//        return null;
//    }


//    public void insertTargetSession(int targetId, long timeFinish, long duration) {
//        // Create a ContentValues object with the values to insert
//        ContentValues values = new ContentValues();
//        values.put("target_id", targetId);
//        values.put("time_finish", timeFinish);
//        values.put("duration", duration);
//        // Insert the values into the "coins" table
//        dataBase.insert(DataBase.TARGET_SESSIONS, null, values);
//    }

//    public List<SessionTargetContainer> getSessionsFromPeriod(long period, boolean isGroupBy, int... sessionIds) {
//        if (sessionIds.length > 1) {
//            throw new IllegalArgumentException("Only one session ID is allowed.");
//        }
//
//        int sessionId = (sessionIds.length > 0) ? sessionIds[0] : -1;
//
//        List<SessionTargetContainer> sessions = new ArrayList<>();
//        String[] columns = {"*"};
//        String selection = "time_finish>=?";
//        String[] selectionArgs = new String[1];
//        selectionArgs[0] = String.valueOf(period);
//
//        if (sessionId != -1) {
//            selection += " AND target_id=?";
//            selectionArgs = new String[2];
//            selectionArgs[0] = String.valueOf(period);
//            selectionArgs[1] = String.valueOf(sessionId);
//        }
//        String grouped = null;
//        if (isGroupBy) grouped = " target_id";
//
//        Cursor cursor = dataBase.query(DataBase.TARGET_SESSIONS, columns, selection, selectionArgs, grouped, null, null);
//
//        // Iterate over the rows in the Cursor and extract the data
//        while (cursor.moveToNext()) {
//            int id = cursor.getInt(cursor.getColumnIndexOrThrow("target_id"));
//            long timeFinish = cursor.getInt(cursor.getColumnIndexOrThrow("time_finish"));
//            int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
//            sessions.add(new SessionTargetContainer(id, timeFinish, duration));
//        }
//
//        cursor.close();
//        return sessions;
//    }


//    public void updateCoinInfo(String coinId, String column, String data) {
//        ContentValues values = new ContentValues();
//        values.put(column, data);
//
//        // Define the WHERE clause
//        String selection = "coin_id = ?";
//        String[] selectionArgs = {coinId};
//
//        // Update the rows that match the WHERE clause
//        int count = dataBase.update(DataBase.COIN_TABLE, values, selection, selectionArgs);
//    }

//    public List<HashMap<String, String>> getAllCoins() {
//        List<HashMap<String, String>> coins = new ArrayList<>();
//
//        // Define the columns to retrieve
//        String[] projection = {"*"};
//
//        // Define the WHERE clause
////        String selection = "age > ?";
////        String[] selectionArgs = {"20"};
//
//        // Define the sort order
//        String sortOrder = "position DESC";
//
//        // Query the database and get a Cursor object
//        Cursor cursor = dataBase.query(DataBase.COIN_TABLE, projection, null, null, null, null, sortOrder);
//
//        // Iterate over the rows in the Cursor and extract the data
//        while (cursor.moveToNext()) {
//            HashMap<String, String> coinData = new HashMap<>();
//            String coinId = cursor.getString(cursor.getColumnIndexOrThrow("coin_id"));
//            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
//            String symbol = cursor.getString(cursor.getColumnIndexOrThrow("symbol"));
//            int position = cursor.getInt(cursor.getColumnIndexOrThrow("position"));
//            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
//            coinData.put("name", name);
//            coinData.put("position", String.valueOf(position));
//            coinData.put("id", String.valueOf(id));
//            coinData.put("coin_id", coinId);
//            coinData.put("symbol", symbol);
//            coins.add(coinData);
//            // Do something with the data...
//        }
//
//        // Close the Cursor when you're done
//        cursor.close();
//        return coins;
//    }

//    public int getRowCount() {
//        Cursor cursor = dataBase.rawQuery("SELECT COUNT(*) FROM " + DataBase.TARGETS_TABLE, null);
//        cursor.moveToFirst();
//        int count = cursor.getInt(0);
//        cursor.close();
//        return count;
//    }

//    public boolean isCoinInDataBase(String coinIdName) {
//        String query = "SELECT * FROM coins WHERE coin_id = ?";
//        String[] selectionArgs = {coinIdName};
//        Cursor cursor = dataBase.rawQuery(query, selectionArgs);
//        int count = cursor.getCount();
//        cursor.close();
//        return count > 0;
//    }

//    public void deleteCoin(String coin_id) {
//        // Define the WHERE clause
//        String selection = "coin_id = ?";
//        String[] selectionArgs = {coin_id};
//        // Delete the rows that match the WHERE clause
//        int count = dataBase.delete(DataBase.COIN_TABLE, selection, selectionArgs);
//        notifySubscribers();
//    }

//    public long maxPosition() {
//        String[] projection = {"MAX(position) AS max_pos"};
//        // Query the database and get a Cursor object
//        Cursor cursor = dataBase.query(DataBase.COIN_TABLE, projection, null, null, null, null, null);
//
//        // Extract the maximum ID value from the Cursor
//        long maxId = -1;
//        if (cursor.moveToFirst()) {
//            maxId = cursor.getLong(cursor.getColumnIndexOrThrow("max_pos"));
//        }
//
//        // Close the Cursor when you're done
//        cursor.close();
//        return maxId;
//    }

//    public CoinDataContainer selectCoinByPosition(long position) {
//        // Define the columns to retrieve
//        String[] projection = {"coin_id", "name", "price", "position", "image"};
//
//        // Define the WHERE clause
//        String selection = null;
//        String[] selectionArgs = null;
//
//        // Define the sort order
//        String sortOrder = "id ASC";
//
//        // Define the position of the row to select
//
//        // Define the number of rows to retrieve (in this case, only 1)
//        int limit = 1;
//
//        // Query the database and get a Cursor object
//        Cursor cursor = dataBase.query(DataBase.COIN_TABLE, projection, selection, selectionArgs, null, null, sortOrder, position + "," + limit);
//        CoinDataContainer coin_data = new CoinDataContainer();
//        // Extract the data from the Cursor
//        if (cursor.moveToFirst()) {
//            String coin_id = cursor.getString(cursor.getColumnIndexOrThrow("coin_id"));
//            int coin_position = cursor.getInt(cursor.getColumnIndexOrThrow("position"));
//            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
//            String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
//            String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
//            coin_data.put("coin_id", coin_id);
//            coin_data.put("position", String.valueOf(coin_position));
//            coin_data.put("name", name);
//            coin_data.put("price", price);
//            coin_data.put("image", image);
//            return coin_data;
//        }
//
//        // Close the Cursor when you're done
//        cursor.close();
//        return null;
//    }

//    public void addListener(Subscriber listener) {
//        subscribers.add(listener);
//    }
//
//    public void removeListener(Subscriber listener) {
//        subscribers.remove(listener);
//    }
//
//    private void notifySubscribers() {
//        for (Subscriber listener : subscribers) {
//            listener.updateData();
//        }
//    }
}

