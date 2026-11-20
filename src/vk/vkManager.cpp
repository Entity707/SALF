#include "vkManager.h"

#include <iostream>

#include "vkCleanup.h"
#include "vkInit.h"
#include "vkRender.h"

int VkManager::init() {
    //TODO:log this or something
    if (0 != salf::vk::detail::deviceInitialization(initData)) return -1;
    if (0 != salf::vk::detail::createSwapchain(initData)) return -1;
    if (0 != salf::vk::detail::getQueues(initData, renderData)) return -1;
    if (0 != salf::vk::detail::createRenderPass(initData, renderData)) return -1;
    if (0 != salf::vk::detail::createGraphicsPipeline(initData, renderData)) return -1;
    if (0 != salf::vk::detail::createCommandPool(initData, renderData)) return -1;
    if (0 != salf::vk::detail::createFrameData(initData, renderData)) return -1;
    if (0 != salf::vk::detail::createSwapchainImageData(initData, renderData)) return -1;

    while (!glfwWindowShouldClose(initData.window)) {
        glfwPollEvents();
        int res = salf::vk::detail::drawFrame(initData, renderData);
        if (res != 0) {
            std::cout << "failed to draw frame \n";
            // return -1;
        }
    }
    initData.disp.deviceWaitIdle();

    salf::vk::detail::cleanup(/*init, renderData*/);
    return 0;
}
