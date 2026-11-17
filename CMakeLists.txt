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

set(ABSL_PROPAGATE_CXX_STD ON CACHE BOOL "" FORCE)
add_subdirectory(external/abseil EXCLUDE_FROM_ALL)

set(HWY_ENABLE_TESTS OFF CACHE BOOL "" FORCE)
set(HWY_ENABLE_EXAMPLES OFF CACHE BOOL "" FORCE)
set(HWY_ENABLE_CONTRIB OFF CACHE BOOL "" FORCE)
add_subdirectory(external/highway EXCLUDE_FROM_ALL)

add_executable(SALF
        src/main.cpp
        src/gameCore.cpp
        src/gameCore.h
        src/math/fixed.h
        src/math/fastMath.cpp
        src/math/fastMath.h
        src/game/screens/screen.cpp
        src/game/screens/screen.h
)

add_executable(generateSinCache
        tools/sinCacheGenerator.cpp
)
add_executable(generateAtanCache
        tools/atanCacheGenerator.cpp
)

target_include_directories(SALF PRIVATE
        external/glm
        external/vma/include
        external/vma
        external/tinygltf
        external/nanotime
        external/taskflow
)

target_compile_definitions(SALF PRIVATE
        GLM_FORCE_RADIANS
        GLM_FORCE_DEPTH_ZERO_TO_ONE
        TINYGLTF_NO_INCLUDE_JSON
        $<$<PLATFORM_ID:Windows>:NOMINMAX WIN32_LEAN_AND_MEAN>
)

target_link_libraries(SALF PRIVATE
        Vulkan::Vulkan
        glfw
        vk-bootstrap::vk-bootstrap
        nlohmann_json::nlohmann_json
        lfreist-hwinfo::hwinfo
        quill::quill
        miniaudio
        hwy
        absl::flat_hash_map
        absl::flat_hash_set
        absl::inlined_vector
        ${CMAKE_DL_LIBS}
)