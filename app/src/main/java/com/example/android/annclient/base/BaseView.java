package com.example.android.annclient.base;

import android.support.annotation.NonNull;

/**
 * Created by achal on 11/3/18.
 */

public interface BaseView<T> {

    void setPresenter(@NonNull T presenter);

}
