#include "vkCleanup.h"

#include "vkManager.h"


namespace salf::vk::detail {
    void cleanup(InitData &initData, RenderData &data) {
        for (auto &swapchain_image_data: data.swapchainImageData) {
            initData.disp.destroySemaphore(swapchain_image_data.semaphoreFinished, nullptr);
            initData.disp.destroyImageView(swapchain_image_data.swapchainImageView, nullptr);
            initData.disp.destroyFramebuffer(swapchain_image_data.framebuffer, nullptr);
        }
        for (auto &frame_data: data.frameData) {
            initData.disp.destroySemaphore(frame_data.semaphoreAvailable, nullptr);
            initData.disp.destroyFence(frame_data.fenceInFlight, nullptr);
        }

        initData.disp.destroyCommandPool(data.commandPool, nullptr);

        initData.disp.destroyPipeline(data.graphicsPipeline, nullptr);
        initData.disp.destroyPipelineLayout(data.pipelineLayout, nullptr);
        initData.disp.destroyRenderPass(data.renderPass, nullptr);

        vkb::destroy_swapchain(initData.swapchain);
        vkb::destroy_device(initData.device);
        vkb::destroy_surface(initData.instance, initData.surface);
        vkb::destroy_instance(initData.instance);
        glfwDestroyWindow(initData.window);
        glfwTerminate();
    }
}
