cmake_minimum_required(VERSION 4.4)
project(SALF LANGUAGES C CXX)

set(CMAKE_CXX_STANDARD 23)
set(CMAKE_CXX_STANDARD_REQUIRED ON)



find_package(Vulkan REQUIRED)

set(ABSL_PROPAGATE_CXX_STD ON CACHE BOOL "" FORCE)
set(ABSL_BUILD_TESTING OFF CACHE BOOL "" FORCE)
set(ABSL_USE_GOOGLETEST_HEAD OFF CACHE BOOL "" FORCE)
set(ABSL_ENABLE_INSTALL OFF CACHE BOOL "" FORCE)
add_subdirectory(external/abseil EXCLUDE_FROM_ALL)

set(GLFW_BUILD_DOCS OFF CACHE BOOL "" FORCE)
set(GLFW_BUILD_TESTS OFF CACHE BOOL "" FORCE)
set(GLFW_BUILD_EXAMPLES OFF CACHE BOOL "" FORCE)
set(GLFW_INSTALL OFF CACHE BOOL "" FORCE)
add_subdirectory(external/glfw EXCLUDE_FROM_ALL)

set(GLM_TEST_ENABLE OFF CACHE BOOL "" FORCE)
add_subdirectory(external/glm EXCLUDE_FROM_ALL)

set(HWY_ENABLE_CONTRIB OFF CACHE BOOL "" FORCE)
set(HWY_ENABLE_EXAMPLES OFF CACHE BOOL "" FORCE)
set(HWY_ENABLE_TESTS OFF CACHE BOOL "" FORCE)
set(HWY_ENABLE_INSTALL OFF CACHE BOOL "" FORCE)
add_subdirectory(external/highway EXCLUDE_FROM_ALL)

set(HWINFO_BUILD_TESTS OFF CACHE BOOL "" FORCE)
set(HWINFO_BUILD_EXAMPLES OFF CACHE BOOL "" FORCE)
add_subdirectory(external/hwinfo EXCLUDE_FROM_ALL)

set(JSON_BuildTests OFF CACHE BOOL "" FORCE)
set(JSON_Install OFF CACHE BOOL "" FORCE)
set(JSON_MultipleHeaders OFF CACHE BOOL "" FORCE)
add_subdirectory(external/json EXCLUDE_FROM_ALL)

set(MINIAUDIO_BUILD_EXAMPLES OFF CACHE BOOL "" FORCE)
set(MINIAUDIO_BUILD_TESTS OFF CACHE BOOL "" FORCE)
add_subdirectory(external/miniaudio EXCLUDE_FROM_ALL)

add_library(nanotime INTERFACE)
target_include_directories(nanotime INTERFACE external/nanotime)

set(QUILL_BUILD_EXAMPLES OFF CACHE BOOL "" FORCE)
set(QUILL_BUILD_TESTS OFF CACHE BOOL "" FORCE)
set(QUILL_BUILD_BENCHMARKS OFF CACHE BOOL "" FORCE)
set(QUILL_ENABLE_INSTALL OFF CACHE BOOL "" FORCE)
add_subdirectory(external/quill EXCLUDE_FROM_ALL)

set(TF_BUILD_EXAMPLES OFF CACHE BOOL "" FORCE)
set(TF_BUILD_TESTS OFF CACHE BOOL "" FORCE)
set(TF_BUILD_BENCHMARKS OFF CACHE BOOL "" FORCE)
add_subdirectory(external/taskflow EXCLUDE_FROM_ALL)

set(TINYGLTF_BUILD_LOADER_EXAMPLE OFF CACHE BOOL "" FORCE)
set(TINYGLTF_HEADER_ONLY ON CACHE BOOL "" FORCE)
set(TINYGLTF_INSTALL OFF CACHE BOOL "" FORCE)
add_subdirectory(external/tinygltf EXCLUDE_FROM_ALL)

set(VK_BOOTSTRAP_TEST OFF CACHE BOOL "" FORCE)
set(VK_BOOTSTRAP_INSTALL OFF CACHE BOOL "" FORCE)
add_subdirectory(external/vk-bootstrap EXCLUDE_FROM_ALL)

set(VMA_BUILD_DOCUMENTATION OFF CACHE BOOL "" FORCE)
set(VMA_BUILD_SAMPLES OFF CACHE BOOL "" FORCE)
add_subdirectory(external/VulkanMemoryAllocator EXCLUDE_FROM_ALL)

include(ExternalProject)
set(HWLOC_PREFIX      "${CMAKE_CURRENT_BINARY_DIR}/hwloc-build")
set(HWLOC_INSTALL_DIR "${HWLOC_PREFIX}/install")
set(HWLOC_INCLUDE_DIR "${HWLOC_INSTALL_DIR}/include")
set(HWLOC_LIBRARY     "${HWLOC_INSTALL_DIR}/lib/libhwloc.a")

file(MAKE_DIRECTORY "${HWLOC_INCLUDE_DIR}")

ExternalProject_Add(hwloc_external
        SOURCE_DIR        "${CMAKE_CURRENT_SOURCE_DIR}/external/hwloc"
        PREFIX            "${HWLOC_PREFIX}"
        CONFIGURE_COMMAND "${CMAKE_CURRENT_SOURCE_DIR}/external/hwloc/configure"
        --prefix=${HWLOC_INSTALL_DIR}
        --enable-static
        --disable-shared
        --disable-cairo
        --disable-libxml2
        --disable-io
        BUILD_COMMAND     make -j
        INSTALL_COMMAND   make install
        BUILD_BYPRODUCTS  "${HWLOC_LIBRARY}"
)

add_library(hwloc STATIC IMPORTED GLOBAL)
set_target_properties(hwloc PROPERTIES
        IMPORTED_LOCATION             "${HWLOC_LIBRARY}"
        INTERFACE_INCLUDE_DIRECTORIES "${HWLOC_INCLUDE_DIR}"
)

add_dependencies(hwloc hwloc_external)

add_executable(SALF
        src/main.cpp
        src/gameCore.cpp
        src/gameCore.h
        src/math/fixed.h
        src/math/fastMath.cpp
        src/math/fastMath.h
        src/game/screens/screen.h
        src/util/log.cpp
        src/util/log.h
)

add_executable(generateSinCache
        tools/sinCacheGenerator.cpp
)
add_executable(generateAtanCache
        tools/atanCacheGenerator.cpp
)


target_compile_definitions(SALF PRIVATE
        GLM_FORCE_RADIANS
        GLM_FORCE_DEPTH_ZERO_TO_ONE
        TINYGLTF_NO_INCLUDE_JSON
        $<$<PLATFORM_ID:Windows>:NOMINMAX WIN32_LEAN_AND_MEAN>
)

target_include_directories(SALF PRIVATE external/tinygltf)

target_link_libraries(SALF PRIVATE
        Vulkan::Vulkan
        glfw
        vk-bootstrap::vk-bootstrap
        GPUOpen::VulkanMemoryAllocator
        glm::glm
        Taskflow
        hwy
        lfreist-hwinfo::hwinfo
        hwloc
        nlohmann_json::nlohmann_json
        quill::quill
        miniaudio
        nanotime
        absl::flat_hash_map
        absl::flat_hash_set
        absl::inlined_vector
        ${CMAKE_DL_LIBS}
)

target_compile_definitions(SALF PRIVATE PROJECT_ROOT_DIR="${CMAKE_SOURCE_DIR}")

if(APPLE)
    target_link_libraries(SALF PRIVATE
        "-framework IOKit"
        "-framework CoreFoundation"
        "-framework CoreAudio"
        "-framework AudioToolbox"
    )
endif()