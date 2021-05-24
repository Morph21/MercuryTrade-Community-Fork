package com.mercury.platform.core.utils.interceptor;

import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.MercuryConfigManager;
import com.mercury.platform.shared.config.MercuryConfigurationSource;
import com.mercury.platform.shared.config.descriptor.NotificationSettingsDescriptor;
import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;
import com.mercury.platform.shared.messageparser.MessageParser;
import com.mercury.platform.shared.store.MercuryStoreCore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import rx.observers.TestSubscriber;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TradeOutMessagesInterceptorTest {

    @BeforeEach
    void setUp() {
        MercuryConfigManager configuration = new MercuryConfigManager(new MercuryConfigurationSource());
        try {
            configuration.load();
        } catch (Exception ignored) {
        }
        final NotificationSettingsDescriptor descriptor = new NotificationSettingsDescriptor();
        descriptor.setOutNotificationEnable(true);
        configuration.notificationConfiguration().set(descriptor);
        Configuration.set(configuration);
    }

    /**
     * Tests if outgoing trade messages for items (not currency) are detected.
     * The relevant implementation of parsing to descriptors is in {@link MessageParser}.
     */
    @ParameterizedTest
    @MethodSource("provideOutgoingPoetradeItemPurchase")
    void parseOutgoingInternationalPoetradeItemPurchase(String whisper, String actualNickname, String actualItemName,
                                                        double actualCurrencyAmount, String actualCurrencyType,
                                                        int actualLeft, int actualTop, String actualLeague) throws Exception {
        // Given
        final TradeOutMessagesInterceptor interceptor = new TradeOutMessagesInterceptor();
        final TestSubscriber<NotificationDescriptor> notifications = new TestSubscriber<>();
        MercuryStoreCore.newNotificationSubject.subscribe(notifications);
        final String actualSourceString = whisper.substring(("@To " + actualNickname + ": ").length());

        // When
        final boolean matched = interceptor.match(whisper);
        final List<NotificationDescriptor> onNextEvents = notifications.getOnNextEvents();
        final NotificationDescriptor descriptor = onNextEvents.isEmpty() ? null : onNextEvents.get(0);

        // Then
        assertTrue(matched, "The interceptor did not find any matches for this whisper");
        assertNotNull(descriptor);
        assertEquals(actualNickname, descriptor.getWhisperNickname());
        assertEquals(NotificationType.OUT_ITEM_MESSAGE, descriptor.getType());
        assertEquals(descriptor.getClass(), ItemTradeNotificationDescriptor.class);
        assertEquals(actualSourceString, descriptor.getSourceString());

        final ItemTradeNotificationDescriptor tradeDescriptor = (ItemTradeNotificationDescriptor) descriptor;
        assertEquals(actualItemName, tradeDescriptor.getItemName());
        assertEquals(actualTop, tradeDescriptor.getTop());
        assertEquals(actualLeft, tradeDescriptor.getLeft());
        assertEquals(actualCurrencyType, tradeDescriptor.getCurrency());
        assertEquals(actualCurrencyAmount, tradeDescriptor.getCurCount());
        assertEquals(actualLeague, tradeDescriptor.getLeague());

    }

    private static Stream<Arguments> provideOutgoingPoetradeItemPurchase() {
        return Stream.of(
                Arguments.of("@To ClearLudko: Hi, I would like to buy your Plaza Map (T3) listed for 2 chaos in Ultimatum (stash tab \"~price 2 chaos\"; position: left 1, top 1)",
                        "ClearLudko", "Plaza Map (T3)", 2.0, "chaos", 1, 1, "Ultimatum"),
                Arguments.of("@To Hydraulophone: Hi, I would like to buy your level 1 0% Enhance Support listed for 1 chaos in Ultimatum (stash tab \"~b/o 1 chaos\"; position: left 9, top 1)",
                        "Hydraulophone", "level 1 0% Enhance Support", 1.0, "chaos", 9, 1, "Ultimatum"),
                Arguments.of("@To Ultimatum_stop: 안녕하세요, 결전(보관함 탭 \"잼 \", 위치: 왼쪽 12, 상단 12)에 1 chaos(으)로 올려놓은 레벨 1 0% 향상 보조(을)를 구매하고 싶습니다",
                        "Ultimatum_stop", "레벨 1 0% 향상 보조", 1.0, "chaos", 12, 12, "결전"),
                Arguments.of("@To Кекичоид: Здравствуйте, хочу купить у вас уровень 1 11% Улучшитель за 1 chaos в лиге Ультиматум (секция \"ТЦ\"; позиция: 11 столбец, 5 ряд)",
                        "Кекичоид", "уровень 1 11% Улучшитель" /* Level 1 11% Enhance support */, 1.0, "chaos", 11, 5, "Ультиматум")
        );
    }

    private static Stream<Arguments> provideOutgoingThaiPoetradeItemPurchase() {
        // TODO get more samples
        // This is from 2019, might be wrong.
        // สวัสดี, เราต้องการจะชื้อของคุณ Devoto's Devotion Nightmare Bascinet ใน ราคา 130 chaos ใน Betrayal (stash tab "Jewe'"; ตำแหน่ง: ซ้าย 4, บน 5
        return Stream.of();
    }

}