package com.charges;

import net.runelite.api.Client;
import net.runelite.api.FontID;
import net.runelite.api.VarClientStr;
import net.runelite.api.Varbits;
import net.runelite.api.widgets.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ChargeCalc {
    private Client client;
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

    public void showWidget(ChargeConfig config, int total) {
        final int quantity;

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
        final int finalQuantity = quantity > 0 ? quantity : config.amount();

        subtract.setText("-" + String.valueOf(finalQuantity));
        subtract.setAction(0, "Set to");
        subtract.setOnOpListener((JavaScriptCallback) ev -> {
            int nonzero = total - finalQuantity;

            if (nonzero < 0) {
                nonzero = total;
            }
            String amount = String.valueOf(nonzero);
            Objects.requireNonNull(client.getWidget(WidgetInfo.CHATBOX_FULL_INPUT)).setText(amount);
            client.setVarcStrValue(VarClientStr.INPUT_TEXT, amount);
        });

        fill.setText("Fill");
        fill.setAction(0, "Fill");
        fill.setOnOpListener((JavaScriptCallback) ev -> {
            Objects.requireNonNull(client.getWidget(WidgetInfo.CHATBOX_FULL_INPUT)).setText(String.valueOf(total));
            client.setVarcStrValue(VarClientStr.INPUT_TEXT, String.valueOf(total));
        });
    }
}
