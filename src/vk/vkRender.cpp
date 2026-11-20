#include "vkRender.h"

#include <iostream>
#include <vulkan/vulkan_core.h>

#include "vkInit.h"
#include "vkManager.h"

namespace {
    int recreateSwapchain(salf::vk::detail::InitData &initData, salf::vk::detail::RenderData &data) {
        initData.disp.deviceWaitIdle();

        for (auto swapchain_image_data: data.swapchainImageData) {
            initData.disp.destroyFramebuffer(swapchain_image_data.framebuffer, nullptr);
            initData.disp.destroyImageView(swapchain_image_data.swapchainImageView, nullptr);
            initData.disp.destroySemaphore(swapchain_image_data.semaphoreFinished, nullptr);
        }

        if (0 != salf::vk::detail::createSwapchain(initData)) return -1;
        if (0 != salf::vk::detail::createSwapchainImageData(initData, data)) return -1;
        return 0;
    }
}

namespace salf::vk::detail {
    int drawFrame(InitData &initData, RenderData &data) {
        auto &current_frame = data.frameData.at(data.currentFrame);

        initData.disp.waitForFences(1, &current_frame.fenceInFlight, VK_TRUE, UINT64_MAX);
        initData.disp.resetFences(1, &current_frame.fenceInFlight);

        VkResult result = initData.disp.acquireNextImageKHR(
            initData.swapchain, UINT64_MAX, current_frame.semaphoreAvailable, VK_NULL_HANDLE, &data.currentSwapchainImage);

        if (result == VK_ERROR_OUT_OF_DATE_KHR) {
            return recreateSwapchain(initData, data);
        } else if (result != VK_SUCCESS && result != VK_SUBOPTIMAL_KHR) {
            std::cout << "failed to acquire swapchain image. Error " << result << "\n";
            return -1;
        }

        auto &current_swapchain_image = data.swapchainImageData.at(data.currentSwapchainImage);

        if (0 != recordCommandBuffer(initData, data)) return -1;

        VkSubmitInfo submitInfo = {};
        submitInfo.sType = VK_STRUCTURE_TYPE_SUBMIT_INFO;

        VkSemaphore wait_semaphores[] = {current_frame.semaphoreAvailable};
        VkPipelineStageFlags wait_stages[] = {VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT};
        submitInfo.waitSemaphoreCount = 1;
        submitInfo.pWaitSemaphores = wait_semaphores;
        submitInfo.pWaitDstStageMask = wait_stages;

        submitInfo.commandBufferCount = 1;
        submitInfo.pCommandBuffers = &current_frame.commandBuffer;

        VkSemaphore signal_semaphores[] = {current_swapchain_image.semaphoreFinished};
        submitInfo.signalSemaphoreCount = 1;
        submitInfo.pSignalSemaphores = signal_semaphores;

        if (initData.disp.queueSubmit(data.graphicsQueue, 1, &submitInfo, current_frame.fenceInFlight) != VK_SUCCESS) {
            std::cout << "failed to submit draw command buffer\n";
            return -1; //"failed to submit draw command buffer
        }

        VkPresentInfoKHR present_info = {};
        present_info.sType = VK_STRUCTURE_TYPE_PRESENT_INFO_KHR;

        present_info.waitSemaphoreCount = 1;
        present_info.pWaitSemaphores = signal_semaphores;

        VkSwapchainKHR swapChains[] = {initData.swapchain};
        present_info.swapchainCount = 1;
        present_info.pSwapchains = swapChains;

        present_info.pImageIndices = &data.currentSwapchainImage;

        result = initData.disp.queuePresentKHR(data.presentQueue, &present_info);
        if (result == VK_ERROR_OUT_OF_DATE_KHR || result == VK_SUBOPTIMAL_KHR) {
            return recreateSwapchain(initData, data);
        } else if (result != VK_SUCCESS) {
            std::cout << "failed to present swapchain image\n";
            return -1;
        }

        data.currentFrame = (data.currentFrame + 1) % MAX_FRAMES_IN_FLIGHT;
        return 0;
    }
}
