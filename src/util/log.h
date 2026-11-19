#pragma once


#include "quill/Backend.h"
#include "quill/Frontend.h"
#include "quill/Logger.h"
#include "quill/LogMacros.h"
#include "quill/sinks/ConsoleSink.h"
#include "quill/sinks/RotatingFileSink.h"

namespace salf::util {
    enum class LogType {
        PERFORMANCE = 0,
        HARDWARE = 1,
        EVENT = 2
    };

    namespace detail {
        quill::Logger *getLogger();

        std::string_view getLogTypeString(LogType type);

        inline std::string formatLog(char const *message, auto &&... args) {
            return fmtquill::vformat(message, fmtquill::make_format_args(args...));
        }
    }

    void initLoggers();


    void logInfo(LogType type, char const *message, auto &&... args) {
        LOG_INFO(detail::getLogger(), "{}{}",
                 detail::getLogTypeString(type),
                 detail::formatLog(message, std::forward<decltype(args)>(args)...));
    }

    void logWarning(LogType type, char const *message, auto &&... args) {
        LOG_WARNING(detail::getLogger(), "{}{}",
                    detail::getLogTypeString(type),
                    detail::formatLog(message, std::forward<decltype(args)>(args)...));
    }


    void logWarning(char const *message, auto... args) {
        LOG_WARNING(detail::getLogger(), message, args...);
    }

    void logError(char const *message, auto... args) {
        LOG_ERROR(detail::getLogger(), message, args...);
    }
}
