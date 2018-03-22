package com.example.android.annclient.util.schedulers;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;

/**
 * Created by achal on 11/3/18.
 */

public interface BaseSchedulerProvider {

    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io(); //this should be used for network operations

    @NonNull
    Scheduler ui();

}
