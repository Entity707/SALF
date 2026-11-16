cmake_minimum_required(VERSION 4.4)
project(SALF LANGUAGES C CXX)

set(CMAKE_CXX_STANDARD 23)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

find_package(Vulkan REQUIRED)

set(GLFW_BUILD_DOCS OFF CACHE BOOL "" FORCE)
set(GLFW_BUILD_TESTS OFF CACHE BOOL "" FORCE)
set(GLFW_BUILD_EXAMPLES OFF CACHE BOOL "" FORCE)
add_subdirectory(external/glfw EXCLUDE_FROM_ALL)

add_subdirectory(external/vk-bootstrap EXCLUDE_FROM_ALL)

set(JSON_BuildTests OFF CACHE INTERNAL "")
add_subdirectory(external/json EXCLUDE_FROM_ALL)

add_subdirectory(external/hwinfo EXCLUDE_FROM_ALL)

add_subdirectory(external/quill EXCLUDE_FROM_ALL)

set(MINIAUDIO_BUILD_EXAMPLES OFF CACHE BOOL "" FORCE)
set(MINIAUDIO_BUILD_TESTS OFF CACHE BOOL "" FORCE)
add_subdirectory(external/miniaudio EXCLUDE_FROM_ALL)

add_executable(SALF main.cpp)

target_include_directories(SALF PRIVATE external/tinygltf)

target_link_libraries(SALF PRIVATE
        Vulkan::Vulkan
        glfw
        vk-bootstrap::vk-bootstrap
        nlohmann_json::nlohmann_json
        lfreist-hwinfo::hwinfo
        quill::quill
        miniaudio
        ${CMAKE_DL_LIBS}
)