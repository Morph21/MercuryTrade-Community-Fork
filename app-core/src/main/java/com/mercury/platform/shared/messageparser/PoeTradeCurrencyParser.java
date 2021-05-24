package com.mercury.platform.shared.messageparser;

import com.mercury.platform.shared.entity.message.CurrencyTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

class PoeTradeCurrencyParser extends BaseRegexParser {

    private static final String poeCurrencyPattern = "^(.*\\s)?(.+): (.+ to buy your (\\d+(\\.\\d+)?)? (.+) for my (\\d+(\\.\\d+)?)? (.+) in (.*?)\\.\\s*(.*))$";

    public PoeTradeCurrencyParser() {
        super(poeCurrencyPattern);
    }

    @Override
    protected NotificationDescriptor parse(Matcher matcher, String whisper) {
        CurrencyTradeNotificationDescriptor tradeNotification = new CurrencyTradeNotificationDescriptor();
        if (matcher.group(6).contains("&") || matcher.group(6)
                                                     .contains(",")) {  //todo this shit for bulk map
            String bulkItems = matcher.group(4) + " " + matcher.group(6);
            tradeNotification.setItems(Arrays.stream(StringUtils.split(bulkItems, ",&"))
                                             .map(String::trim)
                                             .collect(Collectors.toList()));
        } else {
            tradeNotification.setCurrForSaleCount(Double.parseDouble(matcher.group(4)));
            tradeNotification.setCurrForSaleTitle(matcher.group(6));
        }

        tradeNotification.setWhisperNickname(matcher.group(2));
        tradeNotification.setSourceString(matcher.group(3));
        tradeNotification.setCurCount(Double.parseDouble(matcher.group(7)));
        tradeNotification.setCurrency(matcher.group(9));
        tradeNotification.setLeague(matcher.group(10));
        tradeNotification.setOffer(matcher.group(11));
        tradeNotification.setType(NotificationType.INC_CURRENCY_MESSAGE);
        return tradeNotification;
    }
}
