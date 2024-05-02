package com.charges;

import net.runelite.api.*;
import net.runelite.api.widgets.*;

import java.awt.event.KeyEvent;
import java.util.*;

public class ChargeCalc {
    private final Client client;
    private Widget subtract;
    private Widget fill;
    private enum Area {
        INFERNO,
        COLO,
        TOB,
        COX,
        TOA,
        UNKNOWN,
    }

    /**
     * OSRS keycode -> AWT keycode
     */
    public static final Map<Integer, Integer> keyCodeMap = new HashMap<>();
    static {
        keyCodeMap.put(KeyCode.KC_0, KeyEvent.VK_0);
        keyCodeMap.put(KeyCode.KC_1, KeyEvent.VK_1);
        keyCodeMap.put(KeyCode.KC_2, KeyEvent.VK_2);
        keyCodeMap.put(KeyCode.KC_3, KeyEvent.VK_3);
        keyCodeMap.put(KeyCode.KC_4, KeyEvent.VK_4);
        keyCodeMap.put(KeyCode.KC_5, KeyEvent.VK_5);
        keyCodeMap.put(KeyCode.KC_6, KeyEvent.VK_6);
        keyCodeMap.put(KeyCode.KC_7, KeyEvent.VK_7);
        keyCodeMap.put(KeyCode.KC_8, KeyEvent.VK_8);
        keyCodeMap.put(KeyCode.KC_9, KeyEvent.VK_9);
        keyCodeMap.put(KeyCode.KC_A, KeyEvent.VK_A);
        keyCodeMap.put(KeyCode.KC_ADD, KeyEvent.VK_ADD);
        keyCodeMap.put(KeyCode.KC_ALT, KeyEvent.VK_ALT);
        keyCodeMap.put(KeyCode.KC_B, KeyEvent.VK_B);
        keyCodeMap.put(KeyCode.KC_BACK_QUOTE, KeyEvent.VK_BACK_QUOTE);
        keyCodeMap.put(KeyCode.KC_BACK_SLASH, KeyEvent.VK_BACK_SLASH);
        keyCodeMap.put(KeyCode.KC_BACK_SPACE, KeyEvent.VK_BACK_SPACE);
        keyCodeMap.put(KeyCode.KC_C, KeyEvent.VK_C);
        keyCodeMap.put(KeyCode.KC_CLEAR, KeyEvent.VK_CLEAR);
        keyCodeMap.put(KeyCode.KC_CLOSE_BRACKET, KeyEvent.VK_CLOSE_BRACKET);
        keyCodeMap.put(KeyCode.KC_COMMA, KeyEvent.VK_COMMA);
        keyCodeMap.put(KeyCode.KC_CONTROL, KeyEvent.VK_CONTROL);
        keyCodeMap.put(KeyCode.KC_D, KeyEvent.VK_D);
        keyCodeMap.put(KeyCode.KC_DECIMAL, KeyEvent.VK_DECIMAL);
        keyCodeMap.put(KeyCode.KC_DELETE, KeyEvent.VK_DELETE);
        keyCodeMap.put(KeyCode.KC_DIVIDE, KeyEvent.VK_DIVIDE);
        keyCodeMap.put(KeyCode.KC_DOWN, KeyEvent.VK_DOWN);
        keyCodeMap.put(KeyCode.KC_E, KeyEvent.VK_E);
        keyCodeMap.put(KeyCode.KC_END, KeyEvent.VK_END);
        keyCodeMap.put(KeyCode.KC_ENTER, KeyEvent.VK_ENTER);
        keyCodeMap.put(KeyCode.KC_EQUALS, KeyEvent.VK_EQUALS);
        keyCodeMap.put(KeyCode.KC_ESCAPE, KeyEvent.VK_ESCAPE);
        keyCodeMap.put(KeyCode.KC_F, KeyEvent.VK_F);
        keyCodeMap.put(KeyCode.KC_F1, KeyEvent.VK_F1);
        keyCodeMap.put(KeyCode.KC_F10, KeyEvent.VK_F10);
        keyCodeMap.put(KeyCode.KC_F11, KeyEvent.VK_F11);
        keyCodeMap.put(KeyCode.KC_F12, KeyEvent.VK_F12);
        keyCodeMap.put(KeyCode.KC_F2, KeyEvent.VK_F2);
        keyCodeMap.put(KeyCode.KC_F3, KeyEvent.VK_F3);
        keyCodeMap.put(KeyCode.KC_F4, KeyEvent.VK_F4);
        keyCodeMap.put(KeyCode.KC_F5, KeyEvent.VK_F5);
        keyCodeMap.put(KeyCode.KC_F6, KeyEvent.VK_F6);
        keyCodeMap.put(KeyCode.KC_F7, KeyEvent.VK_F7);
        keyCodeMap.put(KeyCode.KC_F8, KeyEvent.VK_F8);
        keyCodeMap.put(KeyCode.KC_F9, KeyEvent.VK_F9);
        keyCodeMap.put(KeyCode.KC_G, KeyEvent.VK_G);
        keyCodeMap.put(KeyCode.KC_H, KeyEvent.VK_H);
        keyCodeMap.put(KeyCode.KC_HOME, KeyEvent.VK_HOME);
        keyCodeMap.put(KeyCode.KC_I, KeyEvent.VK_I);
        keyCodeMap.put(KeyCode.KC_INSERT, KeyEvent.VK_INSERT);
        keyCodeMap.put(KeyCode.KC_J, KeyEvent.VK_J);
        keyCodeMap.put(KeyCode.KC_K, KeyEvent.VK_K);
        keyCodeMap.put(KeyCode.KC_L, KeyEvent.VK_L);
        keyCodeMap.put(KeyCode.KC_LEFT, KeyEvent.VK_LEFT);
        keyCodeMap.put(KeyCode.KC_M, KeyEvent.VK_M);
        keyCodeMap.put(KeyCode.KC_MINUS, KeyEvent.VK_MINUS);
        keyCodeMap.put(KeyCode.KC_MULTIPLY, KeyEvent.VK_MULTIPLY);
        keyCodeMap.put(KeyCode.KC_N, KeyEvent.VK_N);
        keyCodeMap.put(KeyCode.KC_NUMBER_SIGN, KeyEvent.VK_NUMBER_SIGN);
        keyCodeMap.put(KeyCode.KC_NUMPAD0, KeyEvent.VK_NUMPAD0);
        keyCodeMap.put(KeyCode.KC_NUMPAD1, KeyEvent.VK_NUMPAD1);
        keyCodeMap.put(KeyCode.KC_NUMPAD2, KeyEvent.VK_NUMPAD2);
        keyCodeMap.put(KeyCode.KC_NUMPAD3, KeyEvent.VK_NUMPAD3);
        keyCodeMap.put(KeyCode.KC_NUMPAD4, KeyEvent.VK_NUMPAD4);
        keyCodeMap.put(KeyCode.KC_NUMPAD5, KeyEvent.VK_NUMPAD5);
        keyCodeMap.put(KeyCode.KC_NUMPAD6, KeyEvent.VK_NUMPAD6);
        keyCodeMap.put(KeyCode.KC_NUMPAD7, KeyEvent.VK_NUMPAD7);
        keyCodeMap.put(KeyCode.KC_NUMPAD8, KeyEvent.VK_NUMPAD8);
        keyCodeMap.put(KeyCode.KC_NUMPAD9, KeyEvent.VK_NUMPAD9);
        keyCodeMap.put(KeyCode.KC_O, KeyEvent.VK_O);
        keyCodeMap.put(KeyCode.KC_OPEN_BRACKET, KeyEvent.VK_OPEN_BRACKET);
        keyCodeMap.put(KeyCode.KC_P, KeyEvent.VK_P);
        keyCodeMap.put(KeyCode.KC_PAGE_DOWN, KeyEvent.VK_PAGE_DOWN);
        keyCodeMap.put(KeyCode.KC_PAGE_UP, KeyEvent.VK_PAGE_UP);
        keyCodeMap.put(KeyCode.KC_PERIOD, KeyEvent.VK_PERIOD);
        keyCodeMap.put(KeyCode.KC_Q, KeyEvent.VK_Q);
        keyCodeMap.put(KeyCode.KC_QUOTE, KeyEvent.VK_QUOTE);
        keyCodeMap.put(KeyCode.KC_R, KeyEvent.VK_R);
        keyCodeMap.put(KeyCode.KC_RIGHT, KeyEvent.VK_RIGHT);
        keyCodeMap.put(KeyCode.KC_S, KeyEvent.VK_S);
        keyCodeMap.put(KeyCode.KC_SEMICOLON, KeyEvent.VK_SEMICOLON);
        keyCodeMap.put(KeyCode.KC_SHIFT, KeyEvent.VK_SHIFT);
        keyCodeMap.put(KeyCode.KC_SLASH, KeyEvent.VK_SLASH);
        keyCodeMap.put(KeyCode.KC_SPACE, KeyEvent.VK_SPACE);
        keyCodeMap.put(KeyCode.KC_SUBTRACT, KeyEvent.VK_SUBTRACT);
        keyCodeMap.put(KeyCode.KC_T, KeyEvent.VK_T);
        keyCodeMap.put(KeyCode.KC_TAB, KeyEvent.VK_TAB);
        keyCodeMap.put(KeyCode.KC_U, KeyEvent.VK_U);
        keyCodeMap.put(KeyCode.KC_UP, KeyEvent.VK_UP);
        keyCodeMap.put(KeyCode.KC_V, KeyEvent.VK_V);
        keyCodeMap.put(KeyCode.KC_W, KeyEvent.VK_W);
        keyCodeMap.put(KeyCode.KC_X, KeyEvent.VK_X);
        keyCodeMap.put(KeyCode.KC_Y, KeyEvent.VK_Y);
        keyCodeMap.put(KeyCode.KC_Z, KeyEvent.VK_Z);
    }
    

    public ChargeCalc(Widget parent, Client client) {
        this.client = client;
        if (parent == null) {
            return;
        }
        subtract = parent.createChild(WidgetType.TEXT);
        fill = parent.createChild(WidgetType.TEXT);

        System.out.println(parent.getWidth());
        prep(subtract, parent.getWidth() / 2 - 60, 5);
        prep(fill, parent.getWidth() / 2 + 30, 5);
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

    private Area getArea() {
        if (client.getVarbitValue(Varbits.IN_RAID) != 0) {
            return Area.COX;
        }

        if (client.getVarbitValue(Varbits.THEATRE_OF_BLOOD) != 0) {
            return Area.TOB;
        }

        final int []regions = client.getMapRegions();

        final int []TOA_REGIONS = {
                13455, // Lobby
                14160, // Nexus
                15698, // Crondis
                15700, // Zebak
                14162, // Scabaras
                14164, // Kephri
                15186, // Apmken
                15188, // Baba
                14674, // Wardens
                14672, // Tomb
        };

        final int []TOB_REGIONS = {
                14642, // Lobby
                12613, // Maiden
                13125, // Bloat
                13122, // Nylo
                13123, // Sote
                13379, // Sote 2
                12612, // Xarpus
                12611, // Verzik
        };

        final int INFERNO = 9043;
        final int COLO = 7216;


        if (Arrays.stream(regions).anyMatch(r -> Arrays.stream(TOB_REGIONS).anyMatch(reg -> reg == r))) {
            return Area.TOA;
        }

        if (Arrays.stream(regions).anyMatch(r -> Arrays.stream(TOA_REGIONS).anyMatch(reg -> reg == r))) {
            return Area.TOA;
        }

        if (Arrays.stream(regions).anyMatch(r -> r == INFERNO)) {
            return Area.INFERNO;
        }

        if (Arrays.stream(regions).anyMatch(r -> r == COLO)) {
            return Area.COLO;
        }

        return Area.UNKNOWN;
    }

    private int getQuantity(ChargeConfig config, int total) {
        int quantity;

        switch (getArea()) {
            case INFERNO:
                quantity = config.infernoAmount();
                break;
            case COLO:
                quantity = config.coloAmount();
                break;
            case TOA:
                quantity = config.toaAmount();
                break;
            case TOB:
                quantity = config.tobAmount();
                break;
            case COX:
                quantity = config.coxAmount();
                break;
            default:
                quantity = 0;
                break;
        }
        if (quantity == 0) {
            quantity = config.amount();
        }

        final int finalQuantity;
        if (total - quantity >= 0) {
            finalQuantity = total - quantity;
        } else {
            finalQuantity = total;
        }
        return finalQuantity;
    }
    public void showWidget(ChargeConfig config, int total) {
        int quantity = getQuantity(config, total);
        final String quantityStr = String.valueOf(quantity);
        final String totalStr = String.valueOf(total);

        subtract.setText("-" + quantityStr);
        subtract.setAction(0, "Set to");
        subtract.setOnOpListener((JavaScriptCallback) ev -> {
            Objects.requireNonNull(client.getWidget(WidgetInfo.CHATBOX_FULL_INPUT)).setText(quantityStr);
            client.setVarcStrValue(VarClientStr.INPUT_TEXT, quantityStr);
        });

        fill.setText("Fill");
        fill.setAction(0, "Fill");
        fill.setOnOpListener((JavaScriptCallback) ev -> {
            Objects.requireNonNull(client.getWidget(WidgetInfo.CHATBOX_FULL_INPUT)).setText(totalStr);
            client.setVarcStrValue(VarClientStr.INPUT_TEXT, totalStr);
        });

        client.getWidget(WidgetInfo.CHATBOX_FULL_INPUT).setOnKeyListener((JavaScriptCallback) ev -> {
            Integer awtEv = keyCodeMap.getOrDefault(ev.getTypedKeyCode(), -1);
            if (awtEv == config.enterBind().getKeyCode() || awtEv == KeyEvent.VK_ENTER) {
                client.runScript(112, 84, 0, "");
            } else if (awtEv == config.fillBind().getKeyCode()) {
                Objects.requireNonNull(client.getWidget(WidgetInfo.CHATBOX_FULL_INPUT)).setText(totalStr);
                client.setVarcStrValue(VarClientStr.INPUT_TEXT, totalStr);
            } else if (awtEv == config.subBind().getKeyCode()) {
                Objects.requireNonNull(client.getWidget(WidgetInfo.CHATBOX_FULL_INPUT)).setText(quantityStr);
                client.setVarcStrValue(VarClientStr.INPUT_TEXT, quantityStr);
            }
        });

    }
}
