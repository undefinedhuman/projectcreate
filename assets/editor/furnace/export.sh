IFS=$'\n'
SPRITE_NAME='Furnace'

for layer in $(aseprite -b -list-layers $SPRITE_NAME.aseprite) ; do
  aseprite -b -layer "$layer" $SPRITE_NAME.aseprite -sheet "layers/$layer.png"
done

aseprite -b --list-tags --list-layers $SPRITE_NAME.aseprite --data "data.json"