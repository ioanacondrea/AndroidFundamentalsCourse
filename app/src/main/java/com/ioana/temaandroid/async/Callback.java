package com.ioana.temaandroid.async;

public interface Callback<R> {
    void runResultOnUiThread(R result);
}
