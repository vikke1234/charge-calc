package com.charges;

import net.runelite.api.Client;
import net.runelite.api.FontID;
import net.runelite.api.VarClientStr;
import net.runelite.api.widgets.*;


public class ChargeCalc {

    Client client;

    private Widget subtract;

    private Widget fill;
    private Widget all;

    public ChargeCalc(Widget parent, Client client) {
        this.client = client;
        if (parent == null) {
            return;
        }
        this.client = client;
        subtract = parent.createChild(WidgetType.TEXT);
        fill = parent.createChild(WidgetType.TEXT);
        all = parent.createChild(WidgetType.TEXT);

        System.out.println(parent.getWidth());
        prep(subtract, parent.getWidth() / 2 - 60, 5);
        prep(fill, parent.getWidth() / 2 - 15, 5);
        prep(all, parent.getWidth() / 2 + 30, 5);
    }

    private void prep(Widget widget, int x, int y) {
        widget.setTextColor(0x800000);
        widget.setFontId(FontID.VERDANA_11_BOLD);

        widget.setOriginalX(x);
        widget.setOriginalY(y);
        widget.setOriginalHeight(20);
        widget.setOriginalWidth(40);

        widget.setWidthMode(WidgetSizeMode.ABSOLUTE);
        widget.setYPositionMode(WidgetPositionMode.ABSOLUTE_TOP);
        widget.setXTextAlignment(WidgetTextAlignment.CENTER);

        widget.setHasListener(true);
        widget.setOnMouseRepeatListener((JavaScriptCallback) ev -> widget.setTextColor(0xFFFFFF));
        widget.setOnMouseLeaveListener((JavaScriptCallback) ev -> widget.setTextColor(0x800000));

        widget.revalidate();
    }

    public void showWidget(int quantity, int total) {
        subtract.setText("-" + String.valueOf(quantity));
        subtract.setAction(0, "Set to");
        subtract.setOnOpListener((JavaScriptCallback) ev -> {
            String amount = String.valueOf(total - quantity);
            client.getWidget(WidgetInfo.CHATBOX_FULL_INPUT).setText(amount);
            client.setVarcStrValue(VarClientStr.INPUT_TEXT, amount);
        });

        all.setText("All");
        all.setAction(0, "All");
        all.setOnOpListener((JavaScriptCallback) ev -> {
            client.getWidget(WidgetInfo.CHATBOX_FULL_INPUT).setText(String.valueOf(total));
            client.setVarcStrValue(VarClientStr.INPUT_TEXT, String.valueOf(total));
        });

        fill.setText("Fill");
        fill.setAction(0, "Fill");
        fill.setOnOpListener((JavaScriptCallback) ev -> {
            // Script 112 is the keylistener script, 84 is enter, 0 is the visible key, "" is unused on enter
            client.runScript(112, 84, 0, "");
        });
    }
}
