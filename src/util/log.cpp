#include "log.h"

#ifndef PROJECT_ROOT_DIR
#define PROJECT_ROOT_DIR "."
#endif

namespace {
    char const *logTypeStrings[] = {"[PERFORMANCE] ", "[HARDWARE] ", "[EVENT] "};
    quill::Logger *logger = nullptr;
    std::filesystem::path logPath = std::filesystem::path(PROJECT_ROOT_DIR) / "resources" / "logs" / "log.log";

}
namespace salf::util {
    namespace detail {
        quill::Logger* getLogger() {
            return logger;
        }
        std::string_view getLogTypeString(LogType logType) {
            return logTypeStrings[static_cast<std::size_t>(logType)];
        }
    }
    void initLoggers() {
        quill::Backend::start();
        auto consoleSink = quill::Frontend::create_or_get_sink<quill::ConsoleSink>("console_sink");
        auto rotatingFileSink = quill::Frontend::create_or_get_sink<quill::RotatingFileSink>(
            logPath,
            []() {
                quill::RotatingFileSinkConfig cfg;
                cfg.set_open_mode('w');
                cfg.set_timezone(quill::Timezone::GmtTime);
                cfg.set_filename_append_option(quill::FilenameAppendOption::StartCustomTimestampFormat,
                                               "%Y%m%dT%H%M%SZ");
                cfg.set_rotation_max_file_size(104857600);
                cfg.set_rotation_on_creation(true);
                cfg.set_max_backup_files(20);
                return cfg;
            }());
        logger = quill::Frontend::create_or_get_logger(
            "root",
            {std::move(consoleSink), std::move(rotatingFileSink)},
            quill::PatternFormatterOptions{
                "[%(time)] [LOG_%(log_level)] %(message)",
                "%Y-%m-%dT%H:%M:%S.%QmsZ",
                quill::Timezone::GmtTime
            });
    }




}
