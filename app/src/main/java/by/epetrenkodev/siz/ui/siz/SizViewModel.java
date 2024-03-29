package by.epetrenkodev.siz.ui.siz;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import by.epetrenkodev.siz.R;
import by.epetrenkodev.siz.data.SizRepository;

public class SizViewModel extends ViewModel implements SizAdapter.OnSizClickListener {

    public MutableLiveData<List<SizItem>> data = new MutableLiveData<>();
    private List<SizItem> sizList;

    public SizViewModel() {
        loadSizList();
    }

    public void loadSizList() {
        sizList = new SizRepository().read();
        sizList.sort(Comparator.comparing(SizItem::getEndDate));
        data.setValue(sizList);
    }

    @Override
    public void onSizClick(View view, SizItem sizItem, int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sizItem.getBeginDate());

        Bundle args = new Bundle();
        args.putString("name", sizItem.getName());
        args.putInt("month", calendar.get(Calendar.MONTH));
        args.putInt("year", calendar.get(Calendar.YEAR));
        args.putInt("period", sizItem.getPeriod());
        args.putInt("position", position);

        Navigation.findNavController(view).navigate(R.id.nav_siz_update, args);
    }

    public void newSiz(String name, Date beginDate, int period) {
        new SizRepository().create(new SizItem(name, beginDate, period));
    }

    public void removeSiz(int position) {
        SizItem sizItem = sizList.get(position);
        new SizRepository().delete(sizItem);
        loadSizList();
    }

    public void updateSiz(String name, Date beginDate, int period, int position) {
        SizItem newSizItem = new SizItem(name, beginDate, period);
        SizItem removeSizItem = sizList.get(position);
        new SizRepository().delete(removeSizItem);
        new SizRepository().create(newSizItem);
        loadSizList();
    }
}