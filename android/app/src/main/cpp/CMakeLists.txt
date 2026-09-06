cmake_minimum_required(VERSION 3.22.1)
project(harshai)

# Offline inference engine integration point.
# Place a pinned llama.cpp checkout at android/third_party/llama.cpp
# and wire its ggml/llama targets here for the final production build.

add_library(harshai SHARED native_bridge.cpp)
find_library(log-lib log)
target_link_libraries(harshai ${log-lib})
