#!/bin/zsh

IFS=$'\n' layers=($(aseprite -b --list-layers "$1"))

for layer in "${layers[@]}" ; do
  aseprite -b -layer "$layer" "$1" -sheet "$2"/layers/"$layer".png
done

aseprite -b --list-tags --list-layers "$1" --data "$2"/data.json
exit 1