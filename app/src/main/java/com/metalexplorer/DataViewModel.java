package com.metalexplorer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.loki.afro.metallum.entity.Disc;

import java.util.List;

public class DataViewModel extends ViewModel {
    private MutableLiveData<List<Disc>> discList = new MutableLiveData<>();

    public void setDataList(List<Disc> data) {
        discList.setValue(data);
    }

    public LiveData<List<Disc>> getDataList() {
        return discList;
    }
}
