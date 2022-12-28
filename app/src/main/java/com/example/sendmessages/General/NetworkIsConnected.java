package com.example.sendmessages.General;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.sendmessages.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashSet;
import java.util.Set;

/**
 *  Класс отвечает за проверку подключения к сети интернет.
 * **/

public class NetworkIsConnected extends AndroidViewModel {

    private final MutableLiveData<Boolean> mConnected = new MutableLiveData<>();
    public static final int VISIBLE_LONG = Snackbar.LENGTH_LONG;
    public static final int VISIBLE_SHORT = Snackbar.LENGTH_SHORT;
    public static final String NO_CONNECTED_TO_NETWORK = "Нет подключения к интернету";

    public NetworkIsConnected(@NonNull Application application) {
        super(application);

        ConnectivityManager connectivityManager =
                (ConnectivityManager) application
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mConnected.setValue(true);
            return;
        }

        /**
         *  Настраиваем запрос, что мы хотим прослушивать.
         * **/

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();

        /**
         *  Настраиваем обратный вызов,
         *  который будет вызываться системой при наступлении события.
         *  (Отключение / Подключение к сети)
         * **/

        ConnectivityManager.NetworkCallback networkCallback =
                new ConnectivityManager.NetworkCallback() {
                    Set<Network> availableNetwork = new HashSet<Network>();

                    @Override
                    public void onAvailable(@NonNull Network network) {
                        super.onAvailable(network);
                        availableNetwork.add(network);
                    }

                    @Override
                    public void onLost(@NonNull Network network) {
                        super.onLost(network);
                        availableNetwork.remove(network);
                        mConnected.postValue(!availableNetwork.isEmpty());
                    }

                    @Override
                    public void onUnavailable() {
                        super.onUnavailable();
                        availableNetwork.clear();
                        mConnected.postValue(!availableNetwork.isEmpty());
                    }
                };

        /**
         * Регистрируем все наши настройки в менеджере.
         * **/

        connectivityManager
                .registerNetworkCallback(networkRequest, networkCallback);
    }

    @NonNull
    public MutableLiveData<Boolean> getConnected() {
        return mConnected;
    }

    /**
     * Вывод уведомления об отсутствии подключения к сети интернет.
     * **/

    public void setSnackbar(View view, String message, int flag) {
        Snackbar
                .make(view, message, flag)
                .setBackgroundTint(getApplication().getResources().getColor(R.color.color_snackbar_not_connected, null))
                .show();
    }

}
