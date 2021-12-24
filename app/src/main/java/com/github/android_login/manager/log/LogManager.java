package com.github.android_login.manager.log;

import com.github.android_login.service.log.DailyWriterFactory;
import com.github.android_login.service.log.Logger;

public class LogManager {

    public LogManager() {
        Logger logger = new Logger(
                new DailyWriterFactory(
                        "/data/data/com.github.android_login/",
                        "_log.csv",
                        "test1,test2,test3"));
        logger.write(new TestLogEntry());
    }
}
