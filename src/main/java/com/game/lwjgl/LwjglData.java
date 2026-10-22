package com.game.lwjgl;

import com.game.input.Input;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.vulkan.VkCommandBuffer;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkPhysicalDevice;
import org.lwjgl.vulkan.VkQueue;
import tools.jackson.databind.ObjectMapper;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

class LwjglData {
  final int MAX_FRAMES_IN_FLIGHT = 1;
  int renderThreads = 2;
  int instancesPerTask = 1 << 14;
  int taskCount;
  
  ObjectMapper objectMapper;
  ObjectArrayList<Input> recordingInputList = new ObjectArrayList<>();
  ObjectArrayList<Input> activeInputList = new ObjectArrayList<>();
  
  RenderData renderData = new RenderData();
  
  //Window
  int windowWidth;
  int windowHeight;
  GLFWVidMode vidMode;
  long windowHandle;
  PointerBuffer requiredExtensions;
  long surface;
  int currentFrame = 0;
  
  //Shaderc
  Object2LongOpenHashMap<Shaders> shadersToLongHandle = new Object2LongOpenHashMap<>();
  
  //Pipeline
  Object2LongOpenHashMap<PipelineTypes> vulkanPipelines = new Object2LongOpenHashMap<>();
  Object2LongOpenHashMap<ComputePipelineTypes> vulkanComputePipelines = new Object2LongOpenHashMap<>();
  long pipelineLayout2D;
  long descriptorSetLayout2D;
  long descriptorPool;
  long pipelineLayoutCollision;
  long descriptorSetLayoutCollision;
  
  //Device
  VkPhysicalDevice[] vkPhysicalDevices;
  VkPhysicalDevice vkPhysicalDevice;
  VkDevice vkDevice;
  int graphicsQueueFamilyIndex = -1;
  int presentQueueFamilyIndex = -1;
  VkQueue graphicsQueue;
  VkQueue presentQueue;
  
  //Instance
  VkInstance vkInstance;
  
  //Swapchain
  long swapchain;
  long[] swapchainImages;
  long[] swapchainImageViews;
  int swapchainImageFormat;
  int swapchainWidth;
  int swapchainHeight;
  volatile boolean framebufferResized;
  
  //sync
  long[] imageAvailableSemaphores = new long[MAX_FRAMES_IN_FLIGHT];
  long[] renderFinishedSemaphores;
  long[] inFlightFences = new long[MAX_FRAMES_IN_FLIGHT];
  
  //command buffer
  VkCommandBuffer[] commandBuffers = new VkCommandBuffer[MAX_FRAMES_IN_FLIGHT];
  
  long descriptorSet;
  
  long commandPool;                // main-thread pool (primary buffers + one-shot uploads)
  long instanceBuffer, instanceMemory;
  ByteBuffer instanceMapped;
  
  //render
  ExecutorService renderExecutor;
  List<VulkanRenderBatchTask2D> renderTasks;
  ConcurrentLinkedQueue<VulkanThreadState.ThreadState> threadStates = new ConcurrentLinkedQueue<>();
  ThreadLocal<VulkanThreadState.ThreadState> threadState;
  
  //stb
  long fontImage;
  long fontAllocation;
  long fontImageView;
  long fontSampler;
  BakedAtlas[] bakedAtlases;
  
  //vma
  long vmaAllocator;
}
