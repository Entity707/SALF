
#include <iostream>

#include "gameCore.h"
#include "util/log.h"

int main() {
    auto* gameCore = new GameCore();
    salf::util::initLoggers();
    return 0;
}
