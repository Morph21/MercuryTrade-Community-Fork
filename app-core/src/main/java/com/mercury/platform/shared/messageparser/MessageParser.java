package com.mercury.platform.shared.messageparser;

import com.mercury.platform.shared.entity.message.CurrencyTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Parses trade whispers to extract information like
 * {@link NotificationDescriptor#getWhisperNickname() player nickname},
 * {@link ItemTradeNotificationDescriptor#getItemName() item name},
 * {@link ItemTradeNotificationDescriptor#getTabName() stash tab name}
 * and {@link ItemTradeNotificationDescriptor#getCurCount() item price}.
 * <p>
 * The parser does not separate between incoming or outgoing messages,
 * which should be done in something like a
 * {@link com.mercury.platform.core.utils.interceptor.TradeOutMessagesInterceptor TradeOutMessagesInterceptor}
 * or {@link com.mercury.platform.core.utils.interceptor.TradeIncMessagesInterceptor TradeIncMessagesInterceptor} instead.
 */
public class MessageParser {

    // TODO: these patterns are not completed yet
    private final static String poeAppBulkCurrenciesPattern = "^(.*\\s)?(.+): (\\s*?wtb\\s+?(.+?)(\\s+?listed for\\s+?([\\d\\.]+?)\\s+?(.+))?\\s+?in\\s+?(.+?)\\s+?\\(stash\\s+?\"(.*?)\";\\s+?left\\s+?(\\d+?),\\s+?top\\s+(\\d+?)\\)\\s*?(.*))$";
    private final static String strTradeStashTabKorPa = "^(.*\\s)?(.+): (안녕하세요, (.+?)\\s*\\(보관함 탭 \"(.*)\", 위치: 왼쪽 (\\d+), 상단 (\\d+)\\)에\\s+?([\\d\\.]*)\\s*(.*)\\s*\\(으\\)로 올려놓은\\s*(.*)을\\(를\\) 구매하고 싶습니다\\s*(.*))$";
    private final static String strBulkCurrenciesKorP = "^(.*\\s)?(.+): (안녕하세요, (.+?)\\s*에\\s+?올려놓은\\s*(.*)을\\(를\\) 제 ([\\d\\.]*)\\s*(.*)\\s*\\(으\\)로 구매하고 싶습니다\\s*(.*))$";

    private final List<BaseRegexParser> parsers;

    public MessageParser() {
        this.parsers = Arrays.asList(
                new PoeAppItemParser(),
                new PoeTradeStashTabParser(),
                new PoeTradeCurrencyParser(), // Must be before PoeTradeItemParser, because both can parse the same messages
                new PoeMapLiveParser(),
                new PoeTradeItemParser(),
                new PoeTradeItemKoreanParser(),
                new PoeTradeItemRussianParser()
        );
    }

    /**
     * Parse the whisper message
     *
     * @param fullMessage the whisper text, after the {@code "@To"} or {@code "@From"}.
     * @return a NotificationDescriptor with information from the message
     * or {@code null} if the message can't be parsed.
     * Can be different subclasses depending on the message contents, like {@link ItemTradeNotificationDescriptor}
     * or {@link CurrencyTradeNotificationDescriptor}.
     */
    public NotificationDescriptor parse(String fullMessage) {
        for (BaseRegexParser parser : this.parsers) {
            final Optional<NotificationDescriptor> descriptor = parser.tryParse(fullMessage);
            if (descriptor.isPresent()) {
                return descriptor.get();
            }
        }

        return null;
    }
}
