#include "vkShaderCompiler.h"

#include <vulkan/vulkan_core.h>

#include "vkManager.h"

namespace salf::vk::detail {
    VkShaderModule createShaderModule(InitData &initData, const std::vector<char> &code) {
        VkShaderModuleCreateInfo create_info = {};
        create_info.sType = VK_STRUCTURE_TYPE_SHADER_MODULE_CREATE_INFO;
        create_info.codeSize = code.size();
        create_info.pCode = reinterpret_cast<const uint32_t *>(code.data());

        VkShaderModule shaderModule;
        if (initData.disp.createShaderModule(&create_info, nullptr, &shaderModule) != VK_SUCCESS) {
            return VK_NULL_HANDLE; // failed to create shader module
        }

        return shaderModule;
    }
}
