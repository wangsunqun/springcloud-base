import org.springframework.util.StringUtils

//本地调试只打印在控制台，服务器部署控制台和文件都打印
//文件地址为/data/log/app，保存时间为7天。

def final LOCATION = "/data/log/app/"
def final SERVER_NAME = "zuul"
def final SAVE_TIME_RANGE = 7
String ENV = System.getProperty("env")

appender('CONSOLE', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} %relative [%thread] %-5level %logger{36} %X{traceId} - %msg%n"
    }
}

if (StringUtils.isEmpty(ENV)) {
    root(INFO, ["CONSOLE"])
} else {
    appender('FILE-INFO', RollingFileAppender) {
        rollingPolicy(TimeBasedRollingPolicy) {
            fileNamePattern = String.format("%s%s/%s%s%s", LOCATION, SERVER_NAME, "logFile.", ENV, ".%d{yyyy-MM-dd}.log.gz")
            maxHistory = SAVE_TIME_RANGE
        }
    }

    root(INFO, ['CONSOLE', 'FILE-INFO'])
}