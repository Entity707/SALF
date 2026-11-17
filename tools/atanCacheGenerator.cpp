#include <cmath>
#include <cstdint>
#include <iostream>
#include <numbers>

int main() {
    constexpr int ATAN_TABLE_BITS = 9;
    constexpr int ATAN_TABLE_SIZE = (1 << ATAN_TABLE_BITS) + 1; // 513
    constexpr double DENOMINATOR = static_cast<double>(1 << ATAN_TABLE_BITS); // 512.0
    constexpr double FULL_CIRCLE = 65536.0;

    std::cout << "constexpr std::array<uint16_t, ATAN_TABLE_SIZE> ATAN_CACHE = {";

    for (int i = 0; i < ATAN_TABLE_SIZE; ++i) {
        double ratio = i / DENOMINATOR;
        double rad = std::atan(ratio);
        auto raw = static_cast<uint16_t>(std::llround((rad / (2.0 * std::numbers::pi)) * FULL_CIRCLE));

        std::cout << raw;
        if (i + 1 < ATAN_TABLE_SIZE) {
            std::cout << ", ";
        }
    }

    std::cout << "};\n";
    return 0;
}