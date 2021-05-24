package com.mercury.platform.shared.messageparser;

import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;

import java.util.regex.Matcher;

class PoeTradeItemRussianParser extends BaseRegexParser {

    private static final String poeTradeItemRussianRegex = "^\\s*(?<name>.*?)\\Q: Здравствуйте, хочу купить у вас \\E(?<item>.*)\\Q за \\E(?<price>[\\d.]*)\\s(?<currency>.*)\\Q в лиге \\E(?<league>.*)\\Q (секция \"\\E(?<stashtab>[^\"]*)\\Q\"; позиция: \\E(?<left>\\d+)\\Q столбец, \\E(?<top>\\d+)\\Q ряд)\\E";

    public PoeTradeItemRussianParser() {
        super(poeTradeItemRussianRegex);
    }

    @Override
    protected NotificationDescriptor parse(Matcher matcher, String whisper) {
        // Copy-paste of PoeTradeItemKoreanParser.
        ItemTradeNotificationDescriptor tradeNotification = new ItemTradeNotificationDescriptor();
        tradeNotification.setWhisperNickname(matcher.group("name"));
        tradeNotification.setSourceString(matcher.group("stashtab"));
        tradeNotification.setItemName(matcher.group("item"));
        if (matcher.group("price") != null) {
            tradeNotification.setCurCount(Double.parseDouble(matcher.group("price")));
            tradeNotification.setCurrency(matcher.group("currency"));
        } else {
            tradeNotification.setCurCount(0d);
            tradeNotification.setCurrency("???");
        }
        tradeNotification.setLeague(matcher.group("league"));
        tradeNotification.setTabName(matcher.group("stashtab"));
        tradeNotification.setLeft(Integer.parseInt(matcher.group("left")));
        tradeNotification.setTop(Integer.parseInt(matcher.group("top")));
//            tradeNotification.setOffer(matcher.group(12));
        tradeNotification.setType(NotificationType.INC_ITEM_MESSAGE);

        final int indexAfterName = whisper.indexOf(":");
        if (indexAfterName != -1) {
            final int sourceStringStart = indexAfterName + ": ".length();
            tradeNotification.setSourceString(whisper.substring(sourceStringStart));
        }

        return tradeNotification;
    }
}
