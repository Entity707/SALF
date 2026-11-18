#pragma once


#include "quill/Frontend.h"
#include "quill/Logger.h"


namespace salf::util {
    extern quill::Logger* logger;

    void initLoggers();
}
