#pragma once
#include <cstdint>
#include <compare>

namespace salf {
    struct fix_t {
        int32_t num = 0;

        static constexpr int SHIFT = 10;
        static constexpr int32_t SCALE = 1 << SHIFT;

        [[nodiscard]] static constexpr fix_t fromInt(int32_t value) {
            return {value << SHIFT};
        }

        [[nodiscard]] static constexpr fix_t fromFloat(float value) {
            return {static_cast<int32_t>(value * SCALE)};
        }

        [[nodiscard]] constexpr float toFloat() const {
            return static_cast<float>(num) / SCALE;
        }

        [[nodiscard]] constexpr int32_t toInt() const {
            return num >> SHIFT;
        }

        constexpr fix_t operator+(fix_t other) const {
            return {num + other.num};
        }

        constexpr fix_t operator-(fix_t other) const {
            return {num - other.num};
        }

        constexpr fix_t &operator+=(fix_t other) {
            num += other.num;
            return *this;
        }

        constexpr fix_t &operator-=(fix_t other) {
            num -= other.num;
            return *this;
        }

        constexpr fix_t operator*(fix_t other) const {
            return {static_cast<int32_t>((static_cast<int64_t>(num) * other.num) >> SHIFT)};
        }

        constexpr fix_t operator/(fix_t other) const {
            return {static_cast<int32_t>((static_cast<int64_t>(num) << SHIFT) / other.num)};
        }

        constexpr auto operator<=>(const fix_t &) const = default;
    };

    struct fix2_t {
        fix_t x;
        fix_t y;

        constexpr fix2_t operator+(fix2_t other) const {
            return {x + other.x, y + other.y};
        }

        constexpr fix2_t operator-(fix2_t other) const {
            return {x - other.x, y - other.y};
        }

        constexpr fix2_t &operator+=(fix2_t other) {
            x += other.x;
            y += other.y;
            return *this;
        }

        constexpr fix2_t &operator-=(fix2_t other) {
            x -= other.x;
            y -= other.y;
            return *this;
        }

        constexpr bool operator==(const fix2_t &) const = default;
    };

    static_assert(sizeof(fix_t) == 4);
    static_assert(sizeof(fix2_t) == 8);
}
