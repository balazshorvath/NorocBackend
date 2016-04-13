#!/bin/sh
if [ -d "/config" ]; then
    rm -r /config
fi
mkdir /config
mkdir /config/noroc
mkdir /config/noroc/npc

cd ../../

cp -r game/ /config/noroc/

gradle fullBuild
#TODO
