cmake_minimum_required(VERSION 3.22.1)
project(harsh_ai)

include(FetchContent)
FetchContent_Declare(llama_cpp
    GIT_REPOSITORY https://github.com/ggml-org/llama.cpp.git
    GIT_TAG master
)
FetchContent_MakeAvailable(llama_cpp)

add_library(harsh_ai SHARED native-lib.cpp)
find_library(log-lib log)
target_link_libraries(harsh_ai PRIVATE llama ggml ${log-lib})
target_compile_features(harsh_ai PRIVATE cxx_std_17)
