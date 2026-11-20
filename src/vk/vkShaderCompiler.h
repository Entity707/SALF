#pragma once
#include <vulkan/vulkan_core.h>

#include "vkManager.h"

namespace salf::vk::detail {
    VkShaderModule createShaderModule(InitData &initData, const std::vector<char> &code);
}
