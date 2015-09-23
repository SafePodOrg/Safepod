package org.safepodapp.util;

import android.content.Context;
import android.content.res.Resources.Theme;

public class OptionsView {
	private int icon;
	private String optionText;
	public OptionsView(Context context, Theme theme, int option, int icon) {
		super();
		this.icon = icon;
//		context.getResources().getDrawable(icon, theme);
		this.optionText = (String) context.getResources().getText(option);
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public String getOptionText() {
		return optionText;
	}
	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}
}