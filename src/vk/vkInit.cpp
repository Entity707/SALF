#include "vkInit.h"

#include <iostream>
#include <vulkan/vulkan_core.h>

#include "vkManager.h"
#include "GLFW/glfw3.h"

namespace {
    std::vector<char> readFile(const std::string &filename) {
        std::ifstream file(filename, std::ios::ate | std::ios::binary);

        if (!file.is_open()) {
            throw std::runtime_error("failed to open file!");
        }

        size_t file_size = (size_t) file.tellg();
        std::vector<char> buffer(file_size);

        file.seekg(0);
        file.read(buffer.data(), static_cast<std::streamsize>(file_size));

        file.close();

        return buffer;
    }
}
namespace salf::vk::detail {
    GLFWwindow *createWindowGlfw(const char *window_name, bool resize) {
        glfwInit();
        glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API);
        if (!resize) glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        return glfwCreateWindow(1024, 1024, window_name, NULL, NULL);
    }



    VkSurfaceKHR createSurfaceGlfw(VkInstance instance, GLFWwindow *window,
                                     VkAllocationCallbacks *allocator) {
        VkSurfaceKHR surface = VK_NULL_HANDLE;
        VkResult err = glfwCreateWindowSurface(instance, window, allocator, &surface);
        if (err) {
            const char *error_msg;
            int ret = glfwGetError(&error_msg);
            if (ret != 0) {
                std::cout << ret << " ";
                if (error_msg != nullptr) std::cout << error_msg;
                std::cout << "\n";
            }
            surface = VK_NULL_HANDLE;
        }
        return surface;
    }

    int deviceInitialization(InitData &initData) {
        initData.window = createWindowGlfw("Vulkan Triangle", true);

        vkb::InstanceBuilder instance_builder;
        auto instance_ret = instance_builder.use_default_debug_messenger().request_validation_layers().build();
        if (!instance_ret) {
            std::cout << instance_ret.error().message() << "\n";
            return -1;
        }
        initData.instance = instance_ret.value();

        initData.instanceDisp = initData.instance.make_table();

        initData.surface = createSurfaceGlfw(initData.instance, initData.window);

        vkb::PhysicalDeviceSelector phys_device_selector(initData.instance);

        auto phys_device_ret = phys_device_selector.set_surface(initData.surface).select();
        if (!phys_device_ret) {
            std::cout << phys_device_ret.error().message() << "\n";
            if (phys_device_ret.error() == vkb::PhysicalDeviceError::no_suitable_device) {
                const auto &detailed_reasons = phys_device_ret.detailed_failure_reasons();
                if (!detailed_reasons.empty()) {
                    std::cerr << "GPU Selection failure reasons:\n";
                    for (const std::string &reason: detailed_reasons) {
                        std::cerr << reason << "\n";
                    }
                }
            }
            return -1;
        }
        vkb::PhysicalDevice physical_device = phys_device_ret.value();

        vkb::DeviceBuilder device_builder{physical_device};

        auto device_ret = device_builder.build();
        if (!device_ret) {
            std::cout << device_ret.error().message() << "\n";
            return -1;
        }
        initData.device = device_ret.value();

        initData.disp = initData.device.make_table();

        return 0;
    }

    int createSwapchain(InitData &initData) {
        vkb::SwapchainBuilder swapchain_builder{initData.device};
        auto swap_ret = swapchain_builder.set_old_swapchain(initData.swapchain).build();
        if (!swap_ret) {
            std::cout << swap_ret.error().message() << " " << swap_ret.vk_result() << "\n";
            return -1;
        }
        vkb::destroy_swapchain(initData.swapchain);
        initData.swapchain = swap_ret.value();
        return 0;
    }

    int getQueues(InitData &initData, RenderData &data) {
        auto gq = initData.device.get_queue(vkb::QueueType::graphics);
        if (!gq.has_value()) {
            std::cout << "failed to get graphics queue: " << gq.error().message() << "\n";
            return -1;
        }
        data.graphicsQueue = gq.value();

        auto pq = initData.device.get_queue(vkb::QueueType::present);
        if (!pq.has_value()) {
            std::cout << "failed to get present queue: " << pq.error().message() << "\n";
            return -1;
        }
        data.presentQueue = pq.value();
        return 0;
    }

    int createRenderPass(InitData &initData, RenderData &data) {
        VkAttachmentDescription color_attachment = {};
        color_attachment.format = initData.swapchain.image_format;
        color_attachment.samples = VK_SAMPLE_COUNT_1_BIT;
        color_attachment.loadOp = VK_ATTACHMENT_LOAD_OP_CLEAR;
        color_attachment.storeOp = VK_ATTACHMENT_STORE_OP_STORE;
        color_attachment.stencilLoadOp = VK_ATTACHMENT_LOAD_OP_DONT_CARE;
        color_attachment.stencilStoreOp = VK_ATTACHMENT_STORE_OP_DONT_CARE;
        color_attachment.initialLayout = VK_IMAGE_LAYOUT_UNDEFINED;
        color_attachment.finalLayout = VK_IMAGE_LAYOUT_PRESENT_SRC_KHR;

        VkAttachmentReference color_attachment_ref = {};
        color_attachment_ref.attachment = 0;
        color_attachment_ref.layout = VK_IMAGE_LAYOUT_COLOR_ATTACHMENT_OPTIMAL;

        VkSubpassDescription subpass = {};
        subpass.pipelineBindPoint = VK_PIPELINE_BIND_POINT_GRAPHICS;
        subpass.colorAttachmentCount = 1;
        subpass.pColorAttachments = &color_attachment_ref;

        VkSubpassDependency dependency = {};
        dependency.srcSubpass = VK_SUBPASS_EXTERNAL;
        dependency.dstSubpass = 0;
        dependency.srcStageMask = VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT;
        dependency.srcAccessMask = 0;
        dependency.dstStageMask = VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT;
        dependency.dstAccessMask = VK_ACCESS_COLOR_ATTACHMENT_READ_BIT | VK_ACCESS_COLOR_ATTACHMENT_WRITE_BIT;

        VkRenderPassCreateInfo render_pass_info = {};
        render_pass_info.sType = VK_STRUCTURE_TYPE_RENDER_PASS_CREATE_INFO;
        render_pass_info.attachmentCount = 1;
        render_pass_info.pAttachments = &color_attachment;
        render_pass_info.subpassCount = 1;
        render_pass_info.pSubpasses = &subpass;
        render_pass_info.dependencyCount = 1;
        render_pass_info.pDependencies = &dependency;

        if (initData.disp.createRenderPass(&render_pass_info, nullptr, &data.renderPass) != VK_SUCCESS) {
            std::cout << "failed to create render pass\n";
            return -1; // failed to create render pass!
        }
        return 0;
    }



    int createGraphicsPipeline(InitData &initData, RenderData &data) {
        // auto vert_code = readFile(std::string() + "/resources/shaders/triangle.vert.spv");
        // auto frag_code = readFile(std::string() + "/example/shaders/triangle.frag.spv");
        //TODO: compile shaders
        VkShaderModule vert_module = nullptr;//createShaderModule(init, vert_code);
        VkShaderModule frag_module = nullptr;//createShaderModule(init, frag_code);

        if (vert_module == VK_NULL_HANDLE || frag_module == VK_NULL_HANDLE) {
            std::cout << "failed to create shader module\n";
            return -1;
        }

        VkPipelineShaderStageCreateInfo vert_stage_info = {};
        vert_stage_info.sType = VK_STRUCTURE_TYPE_PIPELINE_SHADER_STAGE_CREATE_INFO;
        vert_stage_info.stage = VK_SHADER_STAGE_VERTEX_BIT;
        vert_stage_info.module = vert_module;
        vert_stage_info.pName = "main";

        VkPipelineShaderStageCreateInfo frag_stage_info = {};
        frag_stage_info.sType = VK_STRUCTURE_TYPE_PIPELINE_SHADER_STAGE_CREATE_INFO;
        frag_stage_info.stage = VK_SHADER_STAGE_FRAGMENT_BIT;
        frag_stage_info.module = frag_module;
        frag_stage_info.pName = "main";

        VkPipelineShaderStageCreateInfo shader_stages[] = {vert_stage_info, frag_stage_info};

        VkPipelineVertexInputStateCreateInfo vertex_input_info = {};
        vertex_input_info.sType = VK_STRUCTURE_TYPE_PIPELINE_VERTEX_INPUT_STATE_CREATE_INFO;
        vertex_input_info.vertexBindingDescriptionCount = 0;
        vertex_input_info.vertexAttributeDescriptionCount = 0;

        VkPipelineInputAssemblyStateCreateInfo input_assembly = {};
        input_assembly.sType = VK_STRUCTURE_TYPE_PIPELINE_INPUT_ASSEMBLY_STATE_CREATE_INFO;
        input_assembly.topology = VK_PRIMITIVE_TOPOLOGY_TRIANGLE_LIST;
        input_assembly.primitiveRestartEnable = VK_FALSE;

        VkViewport viewport = {};
        viewport.x = 0.0f;
        viewport.y = 0.0f;
        viewport.width = (float) initData.swapchain.extent.width;
        viewport.height = (float) initData.swapchain.extent.height;
        viewport.minDepth = 0.0f;
        viewport.maxDepth = 1.0f;

        VkRect2D scissor = {};
        scissor.offset = {0, 0};
        scissor.extent = initData.swapchain.extent;

        VkPipelineViewportStateCreateInfo viewport_state = {};
        viewport_state.sType = VK_STRUCTURE_TYPE_PIPELINE_VIEWPORT_STATE_CREATE_INFO;
        viewport_state.viewportCount = 1;
        viewport_state.pViewports = &viewport;
        viewport_state.scissorCount = 1;
        viewport_state.pScissors = &scissor;

        VkPipelineRasterizationStateCreateInfo rasterizer = {};
        rasterizer.sType = VK_STRUCTURE_TYPE_PIPELINE_RASTERIZATION_STATE_CREATE_INFO;
        rasterizer.depthClampEnable = VK_FALSE;
        rasterizer.rasterizerDiscardEnable = VK_FALSE;
        rasterizer.polygonMode = VK_POLYGON_MODE_FILL;
        rasterizer.lineWidth = 1.0f;
        rasterizer.cullMode = VK_CULL_MODE_BACK_BIT;
        rasterizer.frontFace = VK_FRONT_FACE_CLOCKWISE;
        rasterizer.depthBiasEnable = VK_FALSE;

        VkPipelineMultisampleStateCreateInfo multisampling = {};
        multisampling.sType = VK_STRUCTURE_TYPE_PIPELINE_MULTISAMPLE_STATE_CREATE_INFO;
        multisampling.sampleShadingEnable = VK_FALSE;
        multisampling.rasterizationSamples = VK_SAMPLE_COUNT_1_BIT;

        VkPipelineColorBlendAttachmentState colorBlendAttachment = {};
        colorBlendAttachment.colorWriteMask =
                VK_COLOR_COMPONENT_R_BIT | VK_COLOR_COMPONENT_G_BIT | VK_COLOR_COMPONENT_B_BIT |
                VK_COLOR_COMPONENT_A_BIT;
        colorBlendAttachment.blendEnable = VK_FALSE;

        VkPipelineColorBlendStateCreateInfo color_blending = {};
        color_blending.sType = VK_STRUCTURE_TYPE_PIPELINE_COLOR_BLEND_STATE_CREATE_INFO;
        color_blending.logicOpEnable = VK_FALSE;
        color_blending.logicOp = VK_LOGIC_OP_COPY;
        color_blending.attachmentCount = 1;
        color_blending.pAttachments = &colorBlendAttachment;
        color_blending.blendConstants[0] = 0.0f;
        color_blending.blendConstants[1] = 0.0f;
        color_blending.blendConstants[2] = 0.0f;
        color_blending.blendConstants[3] = 0.0f;

        VkPipelineLayoutCreateInfo pipeline_layout_info = {};
        pipeline_layout_info.sType = VK_STRUCTURE_TYPE_PIPELINE_LAYOUT_CREATE_INFO;
        pipeline_layout_info.setLayoutCount = 0;
        pipeline_layout_info.pushConstantRangeCount = 0;

        if (initData.disp.createPipelineLayout(&pipeline_layout_info, nullptr, &data.pipelineLayout) != VK_SUCCESS) {
            std::cout << "failed to create pipeline layout\n";
            return -1; // failed to create pipeline layout
        }

        std::vector<VkDynamicState> dynamic_states = {VK_DYNAMIC_STATE_VIEWPORT, VK_DYNAMIC_STATE_SCISSOR};

        VkPipelineDynamicStateCreateInfo dynamic_info = {};
        dynamic_info.sType = VK_STRUCTURE_TYPE_PIPELINE_DYNAMIC_STATE_CREATE_INFO;
        dynamic_info.dynamicStateCount = static_cast<uint32_t>(dynamic_states.size());
        dynamic_info.pDynamicStates = dynamic_states.data();

        VkGraphicsPipelineCreateInfo pipeline_info = {};
        pipeline_info.sType = VK_STRUCTURE_TYPE_GRAPHICS_PIPELINE_CREATE_INFO;
        pipeline_info.stageCount = 2;
        pipeline_info.pStages = shader_stages;
        pipeline_info.pVertexInputState = &vertex_input_info;
        pipeline_info.pInputAssemblyState = &input_assembly;
        pipeline_info.pViewportState = &viewport_state;
        pipeline_info.pRasterizationState = &rasterizer;
        pipeline_info.pMultisampleState = &multisampling;
        pipeline_info.pColorBlendState = &color_blending;
        pipeline_info.pDynamicState = &dynamic_info;
        pipeline_info.layout = data.pipelineLayout;
        pipeline_info.renderPass = data.renderPass;
        pipeline_info.subpass = 0;
        pipeline_info.basePipelineHandle = VK_NULL_HANDLE;

        if (initData.disp.createGraphicsPipelines(VK_NULL_HANDLE, 1, &pipeline_info, nullptr, &data.graphicsPipeline) !=
            VK_SUCCESS) {
            std::cout << "failed to create pipline\n";
            return -1; // failed to create graphics pipeline
        }

        initData.disp.destroyShaderModule(frag_module, nullptr);
        initData.disp.destroyShaderModule(vert_module, nullptr);
        return 0;
    }

    int createCommandPool(InitData &initData, RenderData &data) {
        VkCommandPoolCreateInfo pool_info = {};
        pool_info.sType = VK_STRUCTURE_TYPE_COMMAND_POOL_CREATE_INFO;
        pool_info.flags = VK_COMMAND_POOL_CREATE_RESET_COMMAND_BUFFER_BIT;
        pool_info.queueFamilyIndex = initData.device.get_queue_index(vkb::QueueType::graphics).value();

        if (initData.disp.createCommandPool(&pool_info, nullptr, &data.commandPool) != VK_SUCCESS) {
            std::cout << "failed to create command pool\n";
            return -1; // failed to create command pool
        }
        return 0;
    }

    int createFrameData(InitData &initData, RenderData &data) {
        data.frameData.resize(MAX_FRAMES_IN_FLIGHT);

        for (auto &frame: data.frameData) {
            VkCommandBufferAllocateInfo allocInfo = {};
            allocInfo.sType = VK_STRUCTURE_TYPE_COMMAND_BUFFER_ALLOCATE_INFO;
            allocInfo.commandPool = data.commandPool;
            allocInfo.level = VK_COMMAND_BUFFER_LEVEL_PRIMARY;
            allocInfo.commandBufferCount = 1;


            if (initData.disp.allocateCommandBuffers(&allocInfo, &frame.commandBuffer) != VK_SUCCESS) {
                return -1; // failed to allocate command buffers;
            }

            VkSemaphoreCreateInfo semaphore_info = {};
            semaphore_info.sType = VK_STRUCTURE_TYPE_SEMAPHORE_CREATE_INFO;

            VkFenceCreateInfo fence_info = {};
            fence_info.sType = VK_STRUCTURE_TYPE_FENCE_CREATE_INFO;
            fence_info.flags = VK_FENCE_CREATE_SIGNALED_BIT;


            if (initData.disp.createSemaphore(&semaphore_info, nullptr, &frame.semaphoreAvailable) != VK_SUCCESS ||
                initData.disp.createFence(&fence_info, nullptr, &frame.fenceInFlight) != VK_SUCCESS) {
                std::cout << "failed to create sync objects\n";
                return -1; // failed to create synchronization objects for a frame
            }
        }

        return 0;
    }

    int createSwapchainImageData(InitData &initData, RenderData &data) {
        data.swapchainImageData.resize(initData.swapchain.image_count);

        auto image_and_views = initData.swapchain.get_images_and_image_views();

        for (uint32_t i = 0; i < initData.swapchain.image_count; i++) {
            data.swapchainImageData[i].swapchainImage = image_and_views->first[i];
            data.swapchainImageData[i].swapchainImageView = image_and_views->second[i];
        }

        for (auto &swapchain_image_data: data.swapchainImageData) {
            VkImageView attachments[] = {swapchain_image_data.swapchainImageView};

            VkFramebufferCreateInfo framebuffer_info = {};
            framebuffer_info.sType = VK_STRUCTURE_TYPE_FRAMEBUFFER_CREATE_INFO;
            framebuffer_info.renderPass = data.renderPass;
            framebuffer_info.attachmentCount = 1;
            framebuffer_info.pAttachments = attachments;
            framebuffer_info.width = initData.swapchain.extent.width;
            framebuffer_info.height = initData.swapchain.extent.height;
            framebuffer_info.layers = 1;

            if (initData.disp.createFramebuffer(&framebuffer_info, nullptr, &swapchain_image_data.framebuffer) !=
                VK_SUCCESS) {
                return -1; // failed to create framebuffer
            }

            VkSemaphoreCreateInfo semaphore_info = {};
            semaphore_info.sType = VK_STRUCTURE_TYPE_SEMAPHORE_CREATE_INFO;

            if (initData.disp.createSemaphore(&semaphore_info, nullptr, &swapchain_image_data.semaphoreFinished) !=
                VK_SUCCESS) {
                std::cout << "failed to create sync objects\n";
                return -1; // failed to create synchronization objects for a frame
            }
        }
        return 0;
    }

    int recordCommandBuffer(InitData &initData, RenderData &data) {
        auto &current_frame_data = data.frameData.at(data.currentFrame);

        VkCommandBufferBeginInfo begin_info = {};
        begin_info.sType = VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO;

        if (initData.disp.beginCommandBuffer(current_frame_data.commandBuffer, &begin_info) != VK_SUCCESS) {
            return -1; // failed to begin recording command buffer
        }

        VkRenderPassBeginInfo render_pass_info = {};
        render_pass_info.sType = VK_STRUCTURE_TYPE_RENDER_PASS_BEGIN_INFO;
        render_pass_info.renderPass = data.renderPass;
        render_pass_info.framebuffer = data.swapchainImageData.at(data.currentSwapchainImage).framebuffer;
        render_pass_info.renderArea.offset = {0, 0};
        render_pass_info.renderArea.extent = initData.swapchain.extent;
        VkClearValue clearColor{{{0.0f, 0.0f, 0.0f, 1.0f}}};
        render_pass_info.clearValueCount = 1;
        render_pass_info.pClearValues = &clearColor;

        VkViewport viewport = {};
        viewport.x = 0.0f;
        viewport.y = 0.0f;
        viewport.width = (float) initData.swapchain.extent.width;
        viewport.height = (float) initData.swapchain.extent.height;
        viewport.minDepth = 0.0f;
        viewport.maxDepth = 1.0f;

        VkRect2D scissor = {};
        scissor.offset = {0, 0};
        scissor.extent = initData.swapchain.extent;

        initData.disp.cmdSetViewport(current_frame_data.commandBuffer, 0, 1, &viewport);
        initData.disp.cmdSetScissor(current_frame_data.commandBuffer, 0, 1, &scissor);

        initData.disp.cmdBeginRenderPass(current_frame_data.commandBuffer, &render_pass_info, VK_SUBPASS_CONTENTS_INLINE);

        initData.disp.cmdBindPipeline(current_frame_data.commandBuffer, VK_PIPELINE_BIND_POINT_GRAPHICS,
                                  data.graphicsPipeline);

        initData.disp.cmdDraw(current_frame_data.commandBuffer, 3, 1, 0, 0);

        initData.disp.cmdEndRenderPass(current_frame_data.commandBuffer);

        if (initData.disp.endCommandBuffer(current_frame_data.commandBuffer) != VK_SUCCESS) {
            std::cout << "failed to record command buffer\n";
            return -1; // failed to record command buffer!
        }

        return 0;
    }
}
