#pragma once

#include <fstream>

#include <vulkan/vulkan_core.h>
#include <GLFW/glfw3.h>

#include <VkBootstrap.h>

namespace salf::vk::detail {
    constexpr int MAX_FRAMES_IN_FLIGHT = 2;

    struct InitData {
        GLFWwindow *window;
        vkb::Instance instance;
        vkb::InstanceDispatchTable instanceDisp;
        VkSurfaceKHR surface;
        vkb::Device device;
        vkb::DispatchTable disp;
        vkb::Swapchain swapchain;
    };

    struct FrameData {
        VkFence fenceInFlight;
        VkSemaphore semaphoreAvailable;
        VkCommandBuffer commandBuffer;
    };

    struct SwapchainImageData {
        VkSemaphore semaphoreFinished;
        VkImage swapchainImage;
        VkImageView swapchainImageView;
        VkFramebuffer framebuffer;
    };

    struct RenderData {
        VkQueue graphicsQueue;
        VkQueue presentQueue;

        VkRenderPass renderPass;
        VkPipelineLayout pipelineLayout;
        VkPipeline graphicsPipeline;

        VkCommandPool commandPool;
        std::vector<FrameData> frameData;

        std::vector<SwapchainImageData> swapchainImageData;

        size_t currentFrame = 0;
        uint32_t currentSwapchainImage = 0;
    };
}


class VkManager {
public:
    int init();
private:
    salf::vk::detail::InitData initData;
    salf::vk::detail::RenderData renderData;
};


