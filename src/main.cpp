
#include <iostream>

#include "gameCore.h"
#include "util/log.h"

int main() {
    salf::util::initLoggers();
    auto* gameCore = new GameCore();

    return 0;
}
