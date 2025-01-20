#! /usr/bin/env bash

show_usage_and_exit () {
    printf "USAGE: $1 <source pathname (.k)>\n"
    exit 1
}

if [[ $# != 1 ]]; then
    show_usage_and_exit "$0"
fi

# Get the source path's base and type extention, checking the latter.
sourcePath=$1
extension=${sourcePath##*.}
if [[ ${extension} != "k" ]]; then
    show_usage_and_exit "$0"
fi
base=$(basename ${sourcePath} .${extension})

assemblyPath=${base}.asm
objectPath=${base}.o
executable=${base}
compilationLogPath=${base}.compile.log
assemblyLogPath=${base}.assemble.log
linkLogPath=${base}.link.log

# Compile
java Compiler ${sourcePath} > ${compilationLogPath} 2>&1
if [[ $? != 0 ]]; then
    printf "ERROR: Compilation failed\n"
    cat ${compilationLogPath}
    exit 1
fi
    
# Assemble
nasm -felf64 -gdwarf ${assemblyPath} > ${assemblyLogPath} 2>&1
if [[ $? != 0 ]]; then
    printf "ERROR: Assembly failed\n"
    cat ${assemblyLogPath}
    exit 1
fi

# Link
gcc -ggdb -no-pie -z noexecstack -o ${executable} ${objectPath} > ${linkLogPath} 2>&1
if [[ $? != 0 ]]; then
    printf "ERROR: Linking failed\n"
    cat ${linkLogPath}
    exit 1
fi
