package com.immagine.workok;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.tghsistemas.truckbu.gcm.GcmRegistrationFragment;

/**
 * Utility class for easy access to commonly used preference data units.
 *
 * @author Eduardo Naveda
 */
public class PreferencesUtil {

    private static final String TRUCKBU_PREFERENCES = "truckbu_preferences";

    private Context context;

    public PreferencesUtil(Context context) {
        this.context = context;
    }

    public void setAccessToken(String accessToken) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Const.ACCESS_TOKEN, accessToken);
        editor.commit();
    }

    public String getAccessToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Const.ACCESS_TOKEN, null);
    }

    public void setTokenType(String tokenType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Const.TOKEN_TYPE, tokenType);
        editor.commit();
    }

    public String getTokenType() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Const.TOKEN_TYPE, null);
    }

    public void setClient(String client) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Const.CLIENT, client);
        editor.commit();
    }

    public String getClient() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Const.CLIENT, null);
    }

    public void setExpiry(String expiry) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Const.EXPIRY, expiry);
        editor.commit();
    }

    public String getExpiry() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Const.EXPIRY, null);
    }

    public void setUid(String uid) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Const.UID, uid);
        editor.commit();
    }

    public String getUid() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Const.UID, null);
    }


    public void setMyUserData(String myUserData) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Const.MY_USER, myUserData);
        editor.commit();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor2 = preferences.edit();
        editor2.putString(Const.MY_USER, myUserData);
        editor2.commit();
    }

    public String getMyUserData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Const.MY_USER, null);
    }

    public void setMyProfileData(String myProfileData) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Const.MY_PROFILE, myProfileData);
        editor.commit();
    }

    public String getMyProfileData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Const.MY_PROFILE, null);
    }

    public void setMyContactsData(String myContactsData) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Const.MY_CONTACTS, myContactsData);
        editor.commit();
    }

    public String getMyContactsData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Const.MY_CONTACTS, null);
    }

    public void setMyContactsAddressesData(String addressesData) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Const.MY_CONTACTS_ADDRESSES, addressesData);
        editor.commit();
    }

    public String getMyContactsAddressesData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Const.MY_CONTACTS_ADDRESSES, null);
    }

    public void setMyContactsPhoneNumbersData(String phoneNumbersData) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Const.MY_CONTACTS_PHONE_NUMBERS, phoneNumbersData);
        editor.commit();
    }

    public String getMyContactsPhoneNumbersData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Const.MY_CONTACTS_PHONE_NUMBERS, null);
    }

    public void setCommonUnits(String commonUnits) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Const.UNITS, commonUnits.toString());
        editor.commit();
    }

    public String getCommonUnits() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Const.UNITS, null);
    }

    public void setMyPreferenceMainServices(Boolean visibility) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Const.MY_MAIN_ACTIONS_SERVICES, visibility);
        editor.commit();
    }

    public Boolean getMyPreferenceMainServices() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Const.MY_MAIN_ACTIONS_SERVICES, true);
    }

    public void setMyPreferenceMainTruckBu(Boolean visibility) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Const.MY_MAIN_ACTIONS_TRUCKBU_INFO, visibility);
        editor.commit();
    }

    public void setMyPreferenceMainDriverMode(Boolean visibility) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Const.MY_MAIN_ACTIONS_DRIVER_MODE, visibility);
        editor.commit();
    }

    public Boolean getMyPreferenceMainDriverMode() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Const.MY_MAIN_ACTIONS_DRIVER_MODE, true);
    }

    public Boolean getMyPreferenceMainTruckBu() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Const.MY_MAIN_ACTIONS_TRUCKBU_INFO, true);
    }

    public void setNotifications(String notifications) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Const.NOTIFICATIONS, notifications);
        editor.commit();
    }

    public String getNotifications() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Const.NOTIFICATIONS, null);
    }

    public void setNotificationToken(String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Const.NOTIFICATION_TOKEN, token);
        editor.commit();
    }

    public String getNotificationToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Const.NOTIFICATION_TOKEN, null);
    }

    public void clearNotificationsAndToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Const.NOTIFICATIONS, null);
        editor.putString(Const.NOTIFICATION_TOKEN, null);
        editor.commit();

    }

    public void setTimeLocationSync(String timeLocationSync) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Const.TIME_LOCATION_SYNC, timeLocationSync);
        editor.commit();
    }

    public String getTimeLocationSync() { //15min Default sync
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Const.TIME_LOCATION_SYNC, "5");
    }

    public void setInitializedFluffy(Boolean initializedFluffy) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Const.PREF_INITIALIZED_FLUFFY, initializedFluffy);
        editor.commit();
    }

    public Boolean getInitializedFluffy() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Const.PREF_INITIALIZED_FLUFFY, false);
    }

    public void setInProgressContractedServiceId(Integer contractedServiceId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Const.PREF_CONTRACTED_SERVICE_ID, contractedServiceId);
        editor.commit();
    }

    public Integer getInProgressContractedServiceId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(Const.PREF_CONTRACTED_SERVICE_ID, 0);
    }

    public void setStartedTracking(Boolean startedTracking) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Const.PREF_STARTED_TRACKING, startedTracking);
        editor.commit();
    }

    public Boolean getStartedTracking() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Const.PREF_STARTED_TRACKING, false);
    }

    public void setUpdateLittleFluffy(Boolean updateLittleFluffy) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Const.PREF_UPDATE_FLUFFY, updateLittleFluffy);
        editor.commit();
    }

    public Boolean getUpdateLittleFluffy() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TRUCKBU_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Const.PREF_UPDATE_FLUFFY, false);
    }












    /**
     * Necesario para acceder al servio gcm de Google.
     */
    public static final String PROPERTY_REG_ID = "registration_id";

    /**
     * Version de la App.
     */
    public static final String PROPERTY_APP_VERSION = "app_version";

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private static SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return context.getSharedPreferences(GcmRegistrationFragment.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    public static void storeRegistrationId(Context context, String regId) {
        SharedPreferences prefs = getGcmPreferences(context);
        Log.d("test", "prefs: " + prefs);
        int appVersion = getAppVersion(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Gets the current registration ID for application on GCM com.tghsistemas.ispark.service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    public String getRegistrationId() {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            return "";
        }
        // Check if app was updated; if so, it must unready the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            return "";
        }
        return registrationId;
    }

    /**
     * Remove the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     */
    public void removeRegistrationId() {
        final SharedPreferences prefs = getGcmPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(PROPERTY_REG_ID);
        editor.remove(PROPERTY_APP_VERSION);
        editor.commit();
    }
}
