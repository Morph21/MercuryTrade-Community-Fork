package com.mercury.platform.shared.messageparser;

import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PoeTradeItemRussianParserTest {


    @ParameterizedTest
    @MethodSource("provideOutgoingRussianPoetradeItemPurchase")
    void parseRussianItemTrade(String whisper, String actualNickname, String actualItemName,
                              double actualCurrencyAmount, String actualCurrencyType, int actualLeft, int actualTop, String actualLeague, String actualSourceString) throws Exception {
        // Given
        final PoeTradeItemRussianParser parser = new PoeTradeItemRussianParser();

        // When
        final Optional<NotificationDescriptor> result = parser.tryParse(whisper);

        // Then
        assertTrue(result.isPresent());
        final NotificationDescriptor descriptor = result.get();
        assertEquals(ItemTradeNotificationDescriptor.class, descriptor.getClass());

        final ItemTradeNotificationDescriptor tradeDescriptor = (ItemTradeNotificationDescriptor) descriptor;
        assertEquals(actualNickname, tradeDescriptor.getWhisperNickname());
        assertEquals(actualItemName, tradeDescriptor.getItemName());
        assertEquals(actualCurrencyAmount, tradeDescriptor.getCurCount());
        assertEquals(actualCurrencyType, tradeDescriptor.getCurrency());
        assertEquals(actualLeft, tradeDescriptor.getLeft());
        assertEquals(actualTop, tradeDescriptor.getTop());
        assertEquals(actualSourceString, tradeDescriptor.getSourceString());

        /*
        Leagues:
        "Ультиматум Одна жизнь" - hardcore ultimatum
        "Ультиматум" - ultimatum
        "Стандарт" - standard
        "Одна жизнь" - hardcore
         */
        assertEquals(actualLeague, tradeDescriptor.getLeague());
    }

    private static Stream<Arguments> provideOutgoingRussianPoetradeItemPurchase() {
        return Stream.of(
                Arguments.of(" Кекичоид: Здравствуйте, хочу купить у вас уровень 1 11% Улучшитель за 1 chaos в лиге Ультиматум (секция \"ТЦ\"; позиция: 11 столбец, 5 ряд)",
                        "Кекичоид", "уровень 1 11% Улучшитель" /* Level 1 11% Enhance support */, 1.0, "chaos", 11, 5, "Ультиматум", "Здравствуйте, хочу купить у вас уровень 1 11% Улучшитель за 1 chaos в лиге Ультиматум (секция \"ТЦ\"; позиция: 11 столбец, 5 ряд)"),
                Arguments.of(" Destrim: Здравствуйте, хочу купить у вас Табула раса Матерчатая безрукавка за 1 portal в лиге Ультиматум (секция \"Trade 1\"; позиция: 1 столбец, 1 ряд)",
                        "Destrim", "Табула раса Матерчатая безрукавка" /* Tabula Rasa Simple Robe*/, 1.0, "portal" /*scroll*/, 1, 1, "Ультиматум", "Здравствуйте, хочу купить у вас Табула раса Матерчатая безрукавка за 1 portal в лиге Ультиматум (секция \"Trade 1\"; позиция: 1 столбец, 1 ряд)")
        );
    }
}