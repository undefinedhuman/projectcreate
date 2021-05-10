IFS=$'\n'

for layer in $(aseprite -b -list-layers Player.aseprite) ; do
  aseprite -b -layer "$layer" Player.aseprite -sheet "layers/$layer.png" -data "layers/$layer.json"
done

aseprite -b --list-tags --list-layers Player.aseprite --data "data.json"