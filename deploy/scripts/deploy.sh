#!/bin/sh
if [ -d "/config/noroc" ]; then
    rm -r /config/noroc
fi
if [ ! -d "/config" ]; then
    mkdir /config
fi
mkdir /config/noroc
mkdir /config/noroc/npc

cd ../../

cp -r game/ /config/noroc/

gradle fullBuild
#TODO
