#pragma once
#include <cstdint>

#include "fixed.h"

namespace salf{
    constexpr uint32_t SHIFT = fix_t::SHIFT;
    [[nodiscard]] bool isInRange(fix_t dx, fix_t dy, fix_t rSum)noexcept;
    [[nodiscard]] fix_t sqrt(fix_t value) noexcept;
    [[nodiscard]] fix_t distance(fix_t x1, fix_t y1, fix_t x2, fix_t y2) noexcept;
    [[nodiscard]] fix_t distance(fix_t deltaX, fix_t deltaY) noexcept;
    [[nodiscard]] fix_t sin(angle_t angle) noexcept;
    [[nodiscard]] fix_t cos(angle_t angle) noexcept;
    [[nodiscard]] angle_t atan2(fix_t y, fix_t x) noexcept;
}
