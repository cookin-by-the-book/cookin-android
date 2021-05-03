package com.mobileappdev.cookinbythebook.ui.dashboard;

public class ObservableInteger
{
    private OnIntegerChangeListener listener;

    private int value = 0;
    private String search = "";
    private int changed = 0;

    public void setOnIntegerChangeListener(OnIntegerChangeListener listener)
    {
        this.listener = listener;
    }

    public int get()
    {
        return value;
    }

    public void set(int value)
    {
        this.value = value;

        if(listener != null)
        {
            listener.onIntegerChanged(value);
        }
    }

    public void setSearch(String value) {
        this.search = value;
    }

    public String getSearch() {
        return search;
    }

    public void setChanged(int value) {
        this.changed = value;
    }

    public int getChanged() {
        return changed;
    }
}