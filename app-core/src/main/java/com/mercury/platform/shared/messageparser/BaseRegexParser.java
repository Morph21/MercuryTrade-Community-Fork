package com.mercury.platform.shared.messageparser;

import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class BaseRegexParser {
    private static final Logger log = LogManager.getLogger(BaseRegexParser.class);

    protected Pattern whisperPattern;

    protected BaseRegexParser(String whisperRegex) {
        this.whisperPattern = Pattern.compile(whisperRegex);
    }

    public Optional<NotificationDescriptor> tryParse(String whisper) {
        final Matcher matcher = whisperPattern.matcher(whisper);
        if (matcher.find()) {
            try {
                return Optional.ofNullable(parse(matcher, whisper));
            } catch (Exception e) {
                log.error("Parsing of whisper failed: " + whisper, e);
            }
        }
        return Optional.empty();
    }

    protected abstract NotificationDescriptor parse(Matcher matcher, String whisper);

}
