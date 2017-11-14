/* Copyright 2017 Andrew Dawson
 *
 * This file is a part of Tusky.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * Tusky is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Tusky; if not,
 * see <http://www.gnu.org/licenses>. */

package com.keylesspalace.tusky;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.keylesspalace.tusky.data.CurrentUser;
import com.keylesspalace.tusky.db.AccountEntity;

import java.util.List;

public class SplashActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Determine whether the user is currently logged in, and if so go ahead and load the
         * timeline. Otherwise, start the activity_login screen. */
        SharedPreferences preferences = getSharedPreferences(
                getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        String domain = preferences.getString("domain", null);
        String accessToken = preferences.getString("accessToken", null);

        if (domain != null && accessToken != null) {
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... voids) {
                    List<AccountEntity> accounts = TuskyApplication.getDB().accountDao().getAll();
                    TuskyApplication.getCurrentUser().setActiveAccount(accounts.get(0));
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    startActivity(MainActivity.class);
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            startActivity(LoginActivity.class);
        }
    }

    private void startActivity(Class<? extends Activity> activityClass) {
        startActivity(new Intent(this, activityClass));
        finish();
    }
}
