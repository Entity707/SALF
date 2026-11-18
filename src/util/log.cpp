#include "log.h"


#include "quill/Backend.h"
#include "quill/Frontend.h"
#include "quill/LogMacros.h"
#include "quill/Logger.h"
#include "quill/sinks/ConsoleSink.h"
#include "quill/sinks/RotatingFileSink.h"

#ifndef PROJECT_ROOT_DIR
#define PROJECT_ROOT_DIR "."
#endif

namespace salf::util {
    quill::Logger *logger = nullptr;
    std::filesystem::path logPath = std::filesystem::path(PROJECT_ROOT_DIR) / "resources" / "logs" / "log.log";

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
        // LOG_INFO(logger, "log {}", 1234567789); //log example
    }
}
