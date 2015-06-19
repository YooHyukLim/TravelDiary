package com.example.y.travel_diary.Utils;

import java.util.List;

public interface OnFinishSearchListener {
	public void onSuccess(List<MapSearchItem> itemList);
	public void onFail();
}
