package com.github.noctwm.transmissionrpc.ui;

public interface BaseContract {

    interface BaseView {
    }

    interface Presenter<T> {

        void attachView(T view);

        void viewIsReady();

        void viewIsPaused();

        void detachView();

        void destroy();
    }
}
