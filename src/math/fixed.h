#pragma once

#include <compare>
#include <concepts>
#include <cstdint>

namespace salf {
    template<typename Type>
    concept whole_number = std::integral<Type> && !std::same_as<Type, bool>;

    struct fix_t {
        int32_t num = 0;

        static constexpr int SHIFT = 8;
        static constexpr int32_t SCALE = 1 << SHIFT;
        static constexpr int32_t MASK = SCALE - 1;
        static constexpr int32_t QUARTER = SCALE >> 2;
        static constexpr int32_t HALF = SCALE >> 1;

        [[nodiscard]] static constexpr fix_t fromInt(int32_t value) noexcept {
            return {value << SHIFT};
        }

        [[nodiscard]] static constexpr fix_t fromFloat(float value) noexcept {
            return {static_cast<int32_t>(value * SCALE + (value >= 0.0f ? 0.5f : -0.5f))};
        }

        [[nodiscard]] static constexpr fix_t fromDouble(double value) noexcept {
            return {static_cast<int32_t>(value * SCALE + (value >= 0.0 ? 0.5 : -0.5))};
        }

        [[nodiscard]] constexpr float toFloat() const noexcept {
            return static_cast<float>(num) / SCALE;
        }

        [[nodiscard]] constexpr int32_t toInt() const noexcept {
            return num >> SHIFT;
        }

        [[nodiscard]] constexpr int32_t toIntRound() const noexcept {
            return (num + HALF) >> SHIFT;
        }

        [[nodiscard]] constexpr explicit operator float() const noexcept { return toFloat(); }
        [[nodiscard]] constexpr explicit operator int32_t() const noexcept { return toInt(); }

        [[nodiscard]] constexpr fix_t operator+() const noexcept { return *this; }
        [[nodiscard]] constexpr fix_t operator-() const noexcept { return {-num}; }

        [[nodiscard]] constexpr fix_t operator+(fix_t other) const noexcept { return {num + other.num}; }
        [[nodiscard]] constexpr fix_t operator-(fix_t other) const noexcept { return {num - other.num}; }
        [[nodiscard]] constexpr fix_t operator%(fix_t other) const noexcept { return {num % other.num}; }

        [[nodiscard]] constexpr fix_t operator*(fix_t other) const noexcept {
            int64_t product = static_cast<int64_t>(num) * other.num;
            return {static_cast<int32_t>((product + (product >= 0 ? HALF : HALF - 1)) >> SHIFT)};
        }

        [[nodiscard]] constexpr fix_t operator/(fix_t other) const noexcept {
            int64_t scaled = static_cast<int64_t>(num) << SHIFT;
            int64_t absDivisor = other.num < 0 ? -static_cast<int64_t>(other.num) : other.num;
            int64_t halfDivisor = absDivisor >> 1;
            return {static_cast<int32_t>((scaled + (scaled >= 0 ? halfDivisor : -halfDivisor)) / other.num)};
        }

        friend constexpr fix_t operator+(fix_t left, whole_number auto right) noexcept {
            return {left.num + (static_cast<int32_t>(right) << SHIFT)};
        }

        friend constexpr fix_t operator+(whole_number auto left, fix_t right) noexcept {
            return {(static_cast<int32_t>(left) << SHIFT) + right.num};
        }

        friend constexpr fix_t operator-(fix_t left, whole_number auto right) noexcept {
            return {left.num - (static_cast<int32_t>(right) << SHIFT)};
        }

        friend constexpr fix_t operator-(whole_number auto left, fix_t right) noexcept {
            return {(static_cast<int32_t>(left) << SHIFT) - right.num};
        }

        friend constexpr fix_t operator*(fix_t left, whole_number auto right) noexcept {
            return {static_cast<int32_t>(left.num * right)};
        }

        friend constexpr fix_t operator*(whole_number auto left, fix_t right) noexcept {
            return {static_cast<int32_t>(left * right.num)};
        }

        friend constexpr fix_t operator/(fix_t left, whole_number auto right) noexcept {
            return {static_cast<int32_t>(left.num / right)};
        }

        friend constexpr fix_t operator/(whole_number auto left, fix_t right) noexcept {
            return fromInt(static_cast<int32_t>(left)) / right;
        }

        friend constexpr fix_t operator%(fix_t left, whole_number auto right) noexcept {
            return {left.num % (static_cast<int32_t>(right) << SHIFT)};
        }

        constexpr fix_t &operator+=(fix_t other) noexcept {
            num += other.num;
            return *this;
        }

        constexpr fix_t &operator-=(fix_t other) noexcept {
            num -= other.num;
            return *this;
        }

        constexpr fix_t &operator*=(fix_t other) noexcept { return *this = *this * other; }
        constexpr fix_t &operator/=(fix_t other) noexcept { return *this = *this / other; }

        constexpr fix_t &operator%=(fix_t other) noexcept {
            num %= other.num;
            return *this;
        }

        constexpr fix_t &operator+=(whole_number auto other) noexcept { return *this = *this + other; }
        constexpr fix_t &operator-=(whole_number auto other) noexcept { return *this = *this - other; }
        constexpr fix_t &operator*=(whole_number auto other) noexcept { return *this = *this * other; }
        constexpr fix_t &operator/=(whole_number auto other) noexcept { return *this = *this / other; }
        constexpr fix_t &operator%=(whole_number auto other) noexcept { return *this = *this % other; }

        [[nodiscard]] constexpr fix_t operator&(whole_number auto mask) const noexcept {
            return {num & static_cast<int32_t>(mask)};
        }

        [[nodiscard]] constexpr fix_t operator|(whole_number auto mask) const noexcept {
            return {num | static_cast<int32_t>(mask)};
        }

        [[nodiscard]] constexpr fix_t operator^(whole_number auto mask) const noexcept {
            return {num ^ static_cast<int32_t>(mask)};
        }

        constexpr fix_t &operator&=(whole_number auto mask) noexcept {
            num &= static_cast<int32_t>(mask);
            return *this;
        }

        constexpr fix_t &operator|=(whole_number auto mask) noexcept {
            num |= static_cast<int32_t>(mask);
            return *this;
        }

        constexpr fix_t &operator^=(whole_number auto mask) noexcept {
            num ^= static_cast<int32_t>(mask);
            return *this;
        }

        [[nodiscard]] constexpr fix_t operator<<(int shift) const noexcept {
            return {static_cast<int32_t>(static_cast<uint32_t>(num) << shift)};
        }

        [[nodiscard]] constexpr fix_t operator>>(int shift) const noexcept {
            return {num >> shift};
        }

        constexpr fix_t &operator<<=(int shift) noexcept {
            num = static_cast<int32_t>(static_cast<uint32_t>(num) << shift);
            return *this;
        }

        constexpr fix_t &operator>>=(int shift) noexcept {
            num >>= shift;
            return *this;
        }

        constexpr auto operator<=>(const fix_t &) const = default;
    };

    struct angle_t {
        uint16_t num = 0;

        static constexpr uint16_t QUARTER = 16384;
        static constexpr uint16_t HALF = 32768;

        [[nodiscard]] constexpr explicit operator uint16_t() const noexcept { return num; }
        [[nodiscard]] constexpr explicit operator int32_t() const noexcept { return static_cast<int32_t>(num); }

        [[nodiscard]] constexpr angle_t operator+() const noexcept { return *this; }
        [[nodiscard]] constexpr angle_t operator-() const noexcept { return {static_cast<uint16_t>(-num)}; }

        [[nodiscard]] constexpr angle_t operator+(angle_t other) const noexcept {
            return {static_cast<uint16_t>(num + other.num)};
        }

        [[nodiscard]] constexpr angle_t operator-(angle_t other) const noexcept {
            return {static_cast<uint16_t>(num - other.num)};
        }

        friend constexpr angle_t operator+(angle_t left, whole_number auto right) noexcept {
            return {static_cast<uint16_t>(left.num + right)};
        }

        friend constexpr angle_t operator+(whole_number auto left, angle_t right) noexcept {
            return {static_cast<uint16_t>(left + right.num)};
        }

        friend constexpr angle_t operator-(angle_t left, whole_number auto right) noexcept {
            return {static_cast<uint16_t>(left.num - right)};
        }

        friend constexpr angle_t operator-(whole_number auto left, angle_t right) noexcept {
            return {static_cast<uint16_t>(left - right.num)};
        }

        friend constexpr angle_t operator*(angle_t left, whole_number auto right) noexcept {
            return {static_cast<uint16_t>(left.num * right)};
        }

        friend constexpr angle_t operator*(whole_number auto left, angle_t right) noexcept {
            return {static_cast<uint16_t>(left * right.num)};
        }

        friend constexpr angle_t operator/(angle_t left, whole_number auto right) noexcept {
            return {static_cast<uint16_t>(left.num / right)};
        }

        friend constexpr angle_t operator%(angle_t left, whole_number auto right) noexcept {
            return {static_cast<uint16_t>(left.num % right)};
        }

        constexpr angle_t &operator+=(angle_t other) noexcept {
            num += other.num;
            return *this;
        }

        constexpr angle_t &operator-=(angle_t other) noexcept {
            num -= other.num;
            return *this;
        }

        constexpr angle_t &operator+=(whole_number auto other) noexcept { return *this = *this + other; }
        constexpr angle_t &operator-=(whole_number auto other) noexcept { return *this = *this - other; }
        constexpr angle_t &operator*=(whole_number auto other) noexcept { return *this = *this * other; }
        constexpr angle_t &operator/=(whole_number auto other) noexcept { return *this = *this / other; }
        constexpr angle_t &operator%=(whole_number auto other) noexcept { return *this = *this % other; }

        [[nodiscard]] constexpr angle_t operator&(whole_number auto mask) const noexcept {
            return {static_cast<uint16_t>(num & mask)};
        }

        [[nodiscard]] constexpr angle_t operator|(whole_number auto mask) const noexcept {
            return {static_cast<uint16_t>(num | mask)};
        }

        [[nodiscard]] constexpr angle_t operator^(whole_number auto mask) const noexcept {
            return {static_cast<uint16_t>(num ^ mask)};
        }

        constexpr angle_t &operator&=(whole_number auto mask) noexcept {
            num &= static_cast<uint16_t>(mask);
            return *this;
        }

        constexpr angle_t &operator|=(whole_number auto mask) noexcept {
            num |= static_cast<uint16_t>(mask);
            return *this;
        }

        constexpr angle_t &operator^=(whole_number auto mask) noexcept {
            num ^= static_cast<uint16_t>(mask);
            return *this;
        }

        [[nodiscard]] constexpr angle_t operator<<(int shift) const noexcept {
            return {static_cast<uint16_t>(num << shift)};
        }

        [[nodiscard]] constexpr angle_t operator>>(int shift) const noexcept {
            return {static_cast<uint16_t>(num >> shift)};
        }

        constexpr angle_t &operator<<=(int shift) noexcept {
            num <<= shift;
            return *this;
        }

        constexpr angle_t &operator>>=(int shift) noexcept {
            num >>= shift;
            return *this;
        }

        [[nodiscard]] constexpr int16_t shortestDeltaTo(angle_t target) const noexcept {
            return static_cast<int16_t>(target.num - num);
        }

        [[nodiscard]] constexpr angle_t rotateToward(angle_t target, uint16_t maxStep) const noexcept {
            int16_t delta = shortestDeltaTo(target);
            if (delta > static_cast<int32_t>(maxStep)) return {static_cast<uint16_t>(num + maxStep)};
            if (delta < -static_cast<int32_t>(maxStep)) return {static_cast<uint16_t>(num - maxStep)};
            return target;
        }

        constexpr auto operator<=>(const angle_t &) const = default;
    };
}
