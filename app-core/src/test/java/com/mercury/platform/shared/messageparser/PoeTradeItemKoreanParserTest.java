package com.mercury.platform.shared.messageparser;

import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PoeTradeItemKoreanParserTest {


    @ParameterizedTest
    @MethodSource("provideOutgoingKoreanPoetradeItemPurchase")
    void parseKoreanItemTrade(String whisper, String actualNickname, String actualItemName,
                              double actualCurrencyAmount, String actualCurrencyType, int actualLeft, int actualTop, String actualLeague, String actualSourceString) throws Exception {
        // Given
        final PoeTradeItemKoreanParser parser = new PoeTradeItemKoreanParser();

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
        "하드코어 결전" - hardcore ultimatum
        "결전" - ultimatum
        "스탠다드" - standard
        "하드코어" - hardcore
         */
        assertEquals(actualLeague, tradeDescriptor.getLeague());
    }

    public static Stream<Arguments> provideOutgoingKoreanPoetradeItemPurchase() {
        return Stream.of(
                Arguments.of(" Ultimatum_stop: 안녕하세요, 결전(보관함 탭 \"잼 \", 위치: 왼쪽 12, 상단 12)에 1 chaos(으)로 올려놓은 레벨 1 0% 향상 보조(을)를 구매하고 싶습니다",
                        "Ultimatum_stop", "레벨 1 0% 향상 보조", 1.0, "chaos", 12, 12, "결전", "안녕하세요, 결전(보관함 탭 \"잼 \", 위치: 왼쪽 12, 상단 12)에 1 chaos(으)로 올려놓은 레벨 1 0% 향상 보조(을)를 구매하고 싶습니다"),
                Arguments.of(" 해그톡식: 안녕하세요, 결전(보관함 탭 \"판매\", 위치: 왼쪽 2, 상단 5)에 7 exalted(으)로 올려놓은 레벨 5 23% 각성한 치명적인 상태 이상 보조(을)를 구매하고 싶습니다",
                        "해그톡식", "레벨 5 23% 각성한 치명적인 상태 이상 보조" /* Level 4 23% Awakened Deadly Ailments Support */, 7.0, "exalted", 2, 5, "결전", "안녕하세요, 결전(보관함 탭 \"판매\", 위치: 왼쪽 2, 상단 5)에 7 exalted(으)로 올려놓은 레벨 5 23% 각성한 치명적인 상태 이상 보조(을)를 구매하고 싶습니다"),
                Arguments.of(" 조이맘: 안녕하세요, 결전(보관함 탭 \"~price 1 chaos\", 위치: 왼쪽 1, 상단 2)에 1 chaos(으)로 올려놓은 광장 지도(Plaza Map)(T6)(을)를 구매하고 싶습니다",
                        "조이맘", "광장 지도(Plaza Map)(T6)", 1.0, "chaos", 1, 2, "결전", "안녕하세요, 결전(보관함 탭 \"~price 1 chaos\", 위치: 왼쪽 1, 상단 2)에 1 chaos(으)로 올려놓은 광장 지도(Plaza Map)(T6)(을)를 구매하고 싶습니다")
        );
    }
}