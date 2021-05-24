package com.mercury.platform.shared.messageparser;

import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;

import java.util.regex.Matcher;

class PoeTradeItemKoreanParser extends BaseRegexParser {

    /**
     * Any text between {@code \Q} and {@code \E} will be matched as raw text (escaped).
     */
    private final static String poeTradeKoreanRegex = "^\\s*(?<name>.*?): \\Q안녕하세요, \\E(?<league>.*)\\Q(보관함 탭 \"\\E(?<stashtab>[^\"]*)\\Q\", 위치: 왼쪽 \\E(?<left>\\d+)\\Q, 상단 \\E(?<top>\\d+)\\Q)에 \\E(?<price>[\\d.]*)\\s+(?<currency>.*)\\Q(으)로 올려놓은 \\E(?<item>.*)\\Q(을)를 구매하고 싶습니다\\E";

    public PoeTradeItemKoreanParser() {
        super(poeTradeKoreanRegex);
    }

    @Override
    protected NotificationDescriptor parse(Matcher matcher, String whisper) {
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
