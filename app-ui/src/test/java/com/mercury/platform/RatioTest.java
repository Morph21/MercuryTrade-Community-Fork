package com.mercury.platform;

import com.mercury.platform.ui.misc.Ratio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class RatioTest {
    @Test
    public void testRatio() {
        assertEquals("50:1", Ratio.getRatio(500, 10));
        assertEquals("1:50", Ratio.getRatio(10, 500));
        assertEquals("33:10", Ratio.getRatio(33, 10));
        assertEquals("3:1", Ratio.getRatio(33, 11));
    }
}
