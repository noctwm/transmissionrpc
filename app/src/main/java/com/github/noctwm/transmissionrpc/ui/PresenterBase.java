package com.github.noctwm.transmissionrpc.ui;

public abstract class PresenterBase<T> implements BaseContract.Presenter<T> {

    private T view;

    @Override
    public void attachView(T view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void destroy() {
    }

    protected T getView() {
        return view;
    }

    protected boolean isViewAttached() {
        return view != null;
    }
}
