#pragma once
#include <vulkan/vulkan_core.h>

#include "vkManager.h"
#include "GLFW/glfw3.h"

namespace salf::vk::detail {
    GLFWwindow *createWindowGlfw(const char *window_name = "", bool resize = true);

    VkSurfaceKHR createSurfaceGlfw(VkInstance instance, GLFWwindow *window,
                                   VkAllocationCallbacks *allocator = nullptr);

    int deviceInitialization(InitData &initData);

    int createSwapchain(InitData &initData);

    int getQueues(InitData &initData, RenderData &data);

    int createRenderPass(InitData &initData, RenderData &data);

    int createGraphicsPipeline(InitData &initData, RenderData &data);

    int createCommandPool(InitData &initData, RenderData &data);

    int createFrameData(InitData &initData, RenderData &data);

    int createSwapchainImageData(InitData &initData, RenderData &data);

    int recordCommandBuffer(InitData &initData, RenderData &data);
}
