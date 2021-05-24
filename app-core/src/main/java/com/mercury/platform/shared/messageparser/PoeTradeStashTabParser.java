package com.mercury.platform.shared.messageparser;

import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;

import java.util.regex.Matcher;

class PoeTradeStashTabParser extends BaseRegexParser {

    private static final String poeTradeStashTabPattern = "^(.*\\s)?(.+): (.+ to buy your\\s+?(.+?)(\\s+?listed for\\s+?([\\d\\.]+?)\\s+?(.+))?\\s+?in\\s+?(.+?)\\s+?\\(stash tab \"(.*)\"; position: left (\\d+), top (\\d+)\\)\\s*?(.*))$";

    public PoeTradeStashTabParser() {
        super(poeTradeStashTabPattern);
    }

    @Override
    protected NotificationDescriptor parse(Matcher matcher, String whisper) {
        ItemTradeNotificationDescriptor tradeNotification = new ItemTradeNotificationDescriptor();
        tradeNotification.setWhisperNickname(matcher.group(2));
        tradeNotification.setSourceString(matcher.group(3));
        tradeNotification.setItemName(matcher.group(4));
        if (matcher.group(6) != null) {
            tradeNotification.setCurCount(Double.parseDouble(matcher.group(6)));
            tradeNotification.setCurrency(matcher.group(7));
        } else {
            tradeNotification.setCurCount(0d);
            tradeNotification.setCurrency("???");
        }
        tradeNotification.setLeague(matcher.group(8));
        tradeNotification.setTabName(matcher.group(9));
        tradeNotification.setLeft(Integer.parseInt(matcher.group(10)));
        tradeNotification.setTop(Integer.parseInt(matcher.group(11)));
        tradeNotification.setOffer(matcher.group(12));
        tradeNotification.setType(NotificationType.INC_ITEM_MESSAGE);
        return tradeNotification;
    }
}
