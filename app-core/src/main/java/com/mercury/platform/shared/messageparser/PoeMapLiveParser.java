package com.mercury.platform.shared.messageparser;

import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;

import java.util.Arrays;
import java.util.regex.Matcher;

class PoeMapLiveParser extends BaseRegexParser {

    private static final String poeMapLiveRegex = "^(.*\\s)?(.+): (I'd like to exchange my (.+:\\s\\([\\s\\S,]+) for your (.+:\\s\\([\\S,\\s]+) in\\s+?(.+?)\\.)";

    public PoeMapLiveParser() {
        super(poeMapLiveRegex);
    }

    @Override
    protected NotificationDescriptor parse(Matcher matcher, String whisper) {
        ItemTradeNotificationDescriptor tradeNotification = new ItemTradeNotificationDescriptor();
        tradeNotification.setWhisperNickname(matcher.group(2));
        tradeNotification.setSourceString(matcher.group(3));

        String itemsWanted = matcher.group(5);
        if (itemsWanted.contains(",")) {
            String[] splitItemsWanted = itemsWanted.split(",");
            tradeNotification.setItemsWanted(Arrays.asList(splitItemsWanted));
        }
        tradeNotification.setItemName(itemsWanted);


        String itemsOffered = matcher.group(4);
        if (itemsOffered.contains(",")) {
            String[] splitItemsWanted = itemsOffered.split(",");
            tradeNotification.setItemsOffered(Arrays.asList(splitItemsWanted));
        }
        tradeNotification.setOffer(itemsOffered);


        tradeNotification.setLeague(matcher.group(6));
        tradeNotification.setCurCount(0d);
        tradeNotification.setCurrency(itemsWanted);
        tradeNotification.setType(NotificationType.INC_ITEM_MESSAGE);
        return tradeNotification;
    }
}
