#include <cmath>
#include <cstdint>
#include <iostream>
#include <numbers>

int main() {
    constexpr int32_t SHIFT = 8;
    constexpr int32_t SCALE = 1 << SHIFT;          // 256
    constexpr int SIN_TABLE_BITS = 10;
    constexpr int SIN_TABLE_SIZE = 1 << SIN_TABLE_BITS; // 1024

    std::cout << "constexpr std::array<salf::fix_t, SIN_TABLE_SIZE> SIN_CACHE = {";

    for (int i = 0; i < SIN_TABLE_SIZE; ++i) {
        double angle = (i * 2.0 * std::numbers::pi) / SIN_TABLE_SIZE;
        auto raw = static_cast<int32_t>(std::llround(std::sin(angle) * SCALE));

        std::cout << "salf::fix_t{" << raw << "}";
        if (i + 1 < SIN_TABLE_SIZE) {
            std::cout << ", ";
        }
    }

    std::cout << "};\n";
    return 0;
}