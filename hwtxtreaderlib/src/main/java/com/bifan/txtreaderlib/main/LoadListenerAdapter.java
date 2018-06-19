package com.bifan.txtreaderlib.main;

import com.bifan.txtreaderlib.bean.TxtMsg;
import com.bifan.txtreaderlib.interfaces.ILoadListener;

/**
 * Created by bifan-wei
 * on 2017/12/11.
 */

public class LoadListenerAdapter implements ILoadListener{
    @Override
    public void onSuccess() {
    }

    @Override
    public void onFail(TxtMsg txtMsg) {
    }

    @Override
    public void onMessage(String message) {
    }
}
